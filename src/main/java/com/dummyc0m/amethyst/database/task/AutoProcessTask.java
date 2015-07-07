package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.Amethyst;
import com.dummyc0m.amethyst.database.DataCache;
import com.dummyc0m.amethyst.database.PlayerManager;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class AutoProcessTask implements Runnable {
    private final PlayerManager playerManager;
    private final DataCache dataCache;
    private final Amethyst amethyst;
    private final Server server;
    private final BukkitScheduler scheduler;

    public AutoProcessTask() {
        playerManager = PlayerManager.getInstance();
        dataCache = DataCache.getInstance();
        amethyst = Amethyst.getInstance();
        server = amethyst.getServer();
        scheduler = server.getScheduler();
    }

    @Override
    public void run() {
        Set<Map.Entry<UUID, String>> rawDataEntrySet = dataCache.getRawEntrySet();
        for (Map.Entry<UUID, String> entry : rawDataEntrySet) {
            UUID uuid = entry.getKey();
            if (playerManager.getLoadStage(uuid) != PlayerManager.LoadStage.LOADED) {
                continue;
            }
            String data = entry.getValue();
            if (data == DataCache.EMPTY) {
                cleanUp(uuid);
                processNewPlayer(uuid);
            } else if (data == DataCache.REMOVED) {
                playerManager.setLoadStage(uuid, null);
            } else {
                cleanUp(uuid);
                processPlayer(uuid, data);
            }
        }
    }

    private void processNewPlayer(UUID uuid) {
        //TODO
    }

    private void processPlayer(UUID uuid, String data) {
        //TODO

    }

    private void cleanUp(UUID uuid) {
        playerManager.setLoadStage(uuid, null);
        dataCache.getRawDataMap().remove(uuid);
    }

    private void scheduleAutoSave(UUID uuid) {
        //TODO
    }
}
