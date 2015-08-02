package com.dummyc0m.amethyst.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * Created by Dummyc0m on 7/7/15.
 * synchronized, ideally functional
 */
public class DataConverter {
    private final Server server;
    private final Gson gson;
    private final DataInterface dataInterface;
    private final DataCache dataCache;
    private final Encoder encoder;
    private final Decoder decoder;
    private final JsonParser parser;


    public DataConverter(DataInterface dataInterface, DataCache dataCache, Server server) {
        this.server = server;
        this.gson = new Gson();
        this.dataCache = dataCache;
        this.dataInterface = dataInterface;
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
        parser = new JsonParser();
    }


    public void deserializeRawData(UUID uuid, String data) throws InvalidConfigurationException {
        Player player = server.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            deserializeData(player, parser.parse(data).getAsJsonArray());
        } else {
            dataInterface.queueUnlock(uuid);
        }
    }

    public void deserializeData(Player player, JsonArray jsonArray) throws InvalidConfigurationException {
        //Magic Number
        //Health
        player.setHealthScale(100);
        player.setMaxHealth(100);
        double health = jsonArray.get(0).getAsDouble();
        player.setHealth(health < 100 ? health : 100);

        //Food
        player.setFoodLevel(jsonArray.get(1).getAsInt());
        player.setSaturation(jsonArray.get(2).getAsInt());
        player.setExhaustion(jsonArray.get(3).getAsFloat());

        //Exp
        player.setTotalExperience(jsonArray.get(4).getAsInt());

        //Location
        JsonArray locArray = jsonArray.get(5).getAsJsonArray();
        player.teleport(new Location(server.getWorld(locArray.get(0).getAsString()),
                locArray.get(1).getAsDouble(),
                locArray.get(2).getAsDouble(),
                locArray.get(3).getAsDouble(),
                locArray.get(4).getAsFloat(),
                locArray.get(5).getAsFloat()
        ));

        //Inventory
        player.getInventory().setContents(toItemStacks(jsonArray.get(6)));
        player.getInventory().setArmorContents(toItemStacks(jsonArray.get(7)));
        player.getInventory().setHeldItemSlot(jsonArray.get(8).getAsInt());
        player.getEnderChest().setContents(toItemStacks(jsonArray.get(9)));

        //PotionEffects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffects(toPotionEffects(jsonArray.get(10)));

        //PlayerData
        PlayerData playerData = gson.fromJson(jsonArray.get(11), PlayerData.class);
        playerData.initialize();
        dataCache.cacheData(player.getUniqueId(), playerData);
    }

    public String serializeData(Player player) {
        JsonArray dataArray = new JsonArray();
        dataArray.add(gson.toJsonTree(player.getHealth()));
        dataArray.add(gson.toJsonTree(player.getFoodLevel()));
        dataArray.add(gson.toJsonTree(player.getSaturation()));
        dataArray.add(gson.toJsonTree(player.getExhaustion()));
        dataArray.add(gson.toJsonTree(player.getTotalExperience()));
        dataArray.add(toJson(player.getLocation()));
        dataArray.add(toJson(player.getInventory().getContents()));
        dataArray.add(toJson(player.getInventory().getArmorContents()));
        dataArray.add(gson.toJsonTree(player.getInventory().getHeldItemSlot()));
        dataArray.add(toJson(player.getEnderChest().getContents()));
        dataArray.add(toJson(player.getActivePotionEffects()));
        dataArray.add(gson.toJsonTree(dataCache.getDataMap().get(player.getUniqueId())));
        return dataArray.toString();
    }

    private JsonElement toJson(ItemStack[] itemStacks) {
        YamlConfiguration config = new YamlConfiguration();
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i];
            if (itemStack != null) {
                config.set(Integer.toString(i), itemStack);
            }
        }
        return gson.toJsonTree(encodeBase64(config.saveToString()));
    }

    private JsonElement toJson(Collection<PotionEffect> potionEffects) {
        YamlConfiguration config = new YamlConfiguration();
        Iterator<PotionEffect> potionEffectIterator = potionEffects.iterator();
        int i = 0;
        while (potionEffectIterator.hasNext()) {
            PotionEffect potionEffect = potionEffectIterator.next();
            if (potionEffect != null) {
                config.set(Integer.toString(i), potionEffect);
            }
            i++;
        }
        return gson.toJsonTree(encodeBase64(config.saveToString()));
    }

    private JsonElement toJson(Location location) {
        JsonArray locationArray = new JsonArray();
        locationArray.add(gson.toJsonTree(location.getWorld().getName()));
        locationArray.add(gson.toJsonTree(location.getX()));
        locationArray.add(gson.toJsonTree(location.getY()));
        locationArray.add(gson.toJsonTree(location.getZ()));
        locationArray.add(gson.toJsonTree(location.getYaw()));
        locationArray.add(gson.toJsonTree(location.getPitch()));
        return locationArray;
    }

    private ItemStack[] toItemStacks(JsonElement jsonElement) throws InvalidConfigurationException {
        List<ItemStack> itemStacks = new ArrayList<>();
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(decodeBase64(jsonElement.getAsString()));
        try {
            for (String key : config.getKeys(false)) {
                int index = Integer.parseInt(key);

                while (itemStacks.size() <= index) {
                    itemStacks.add(null);
                }

                itemStacks.set(index, config.getItemStack(key));
            }
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Expected an Integer", e);
        }
        return (ItemStack[]) itemStacks.toArray();
    }

    private Collection<PotionEffect> toPotionEffects(JsonElement jsonElement) throws InvalidConfigurationException {
        List<PotionEffect> potionEffects = new ArrayList<>();
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(decodeBase64(jsonElement.getAsString()));
        config.getKeys(false).forEach(key -> potionEffects.add((PotionEffect) config.get(key)));
        return potionEffects;
    }

    private String encodeBase64(String string) {
        return encoder.encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeBase64(String string) {
        return new String(decoder.decode(string), StandardCharsets.UTF_8);
    }
}
