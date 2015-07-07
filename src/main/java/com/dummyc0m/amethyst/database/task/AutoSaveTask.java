package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.database.DataInterface;
import com.dummyc0m.amethyst.database.PlayerManager;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class AutoSaveTask implements Runnable {
    private final Server server;
    private final UUID uuid;

    public AutoSaveTask(Server server, UUID uuid) {
        this.server = server;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        Player player = server.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            DataInterface.getInstance().queueSave(uuid, false);
        } else {
            server.getScheduler().cancelTask(PlayerManager.getInstance().getAutoSaveIdMap().remove(uuid));
        }
    }
}
