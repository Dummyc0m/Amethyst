package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.database.DataInterface;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 * Ideally Functional
 */
public class AutoSaveTask implements Runnable {
    private final DataInterface dataInterface;
    private final Server server;
    private final UUID uuid;

    public AutoSaveTask(DataInterface dataInterface, Server server, UUID uuid) {
        this.dataInterface = dataInterface;
        this.server = server;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        Player player = server.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            dataInterface.queueSave(player, false);
        } else {
            server.getScheduler().cancelTask(dataInterface.getPlayerManager().getAutoSaveIdMap().remove(uuid));
        }
    }
}
