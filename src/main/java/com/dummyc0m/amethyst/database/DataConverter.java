package com.dummyc0m.amethyst.database;

import com.dummyc0m.amethystcore.AmethystCore;
import com.google.gson.Gson;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class DataConverter {
    private static final DataConverter DATA_CONVERTER = new DataConverter();
    private final Server server = AmethystCore.getInstance().getServer();
    private final Gson gson = new Gson();

    public static DataConverter getInstance() {
        return DATA_CONVERTER;
    }

    public void deserializeRawData(UUID uuid, String data) {
        Player player = server.getPlayer(uuid);
        if (player == null) {
            //TODO
        }
    }

    public String serializeData(Player player) {
        //TODO
        return null;
    }
}
