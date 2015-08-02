package com.dummyc0m.amethyst.database;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 * Ideally functional
 */
public class DataListener implements Listener {
    private final DataInterface dataInterface;
    private final PlayerManager playerManager;

    public DataListener(DataInterface dataInterface) {
        this.dataInterface = dataInterface;
        this.playerManager = dataInterface.getPlayerManager();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerManager.LoadStage stage = playerManager.getLoadStage(uuid);
        if (stage != null && stage != PlayerManager.LoadStage.CONNECTED) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            //TODO Message Configuration
            event.setKickMessage("");
        } else {
            playerManager.setLoadStage(uuid, PlayerManager.LoadStage.CONNECTED);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO Message Configuration
        event.getPlayer().sendMessage("");
        //LoadStage JOINED
        playerManager.setLoadStage(event.getPlayer().getUniqueId(), PlayerManager.LoadStage.JOINED);
        //TODO Time Configuration
        dataInterface.getServer().getScheduler().runTaskLater(dataInterface.getAmethyst(),
                () -> dataInterface.queueLoad(event.getPlayer()),
                20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playerManager.getLoadStage(uuid) == null) {
            dataInterface.queueSave(player, true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageEntity(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        UUID uuid = event.getEntity().getUniqueId();
        if (playerManager.getLoadStage(uuid) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDropItem(PlayerDropItemEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playerManager.getLoadStage(uuid) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playerManager.getLoadStage(uuid) != null) {
            event.setCancelled(true);
        }
    }
}
