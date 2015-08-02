package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.Amethyst;
import com.dummyc0m.amethyst.database.*;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 * Synchronized, ideally functional
 */
public class AutoProcessTask implements Runnable {
    private final DataInterface dataInterface;
    private final PlayerManager playerManager;
    private final DataCache dataCache;
    private final DataConverter dataConverter;
    private final Amethyst amethyst;
    private final Server server;
    private final BukkitScheduler scheduler;

    public AutoProcessTask(DataInterface dataInterface) {
        this.dataInterface = dataInterface;
        this.playerManager = dataInterface.getPlayerManager();
        this.dataCache = dataInterface.getCache();
        this.dataConverter = dataInterface.getDataConverter();
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
            } else {
                cleanUp(uuid);
                processPlayer(uuid, data);
            }
        }
    }

    private void processNewPlayer(UUID uuid) {
        scheduleAutoSave(uuid);
        dataCache.cacheData(uuid, new PlayerData());
        //TODO Notify Player(notification system)
    }

    private void processPlayer(UUID uuid, String data) {
        try {
            dataConverter.deserializeRawData(uuid, data);
            scheduleAutoSave(uuid);
            //TODO Notify Player
        } catch (InvalidConfigurationException e) {
            //TODO Notify Player
            e.printStackTrace();
        }

    }

    private void cleanUp(UUID uuid) {
        playerManager.setLoadStage(uuid, null);
        dataCache.getRawDataMap().remove(uuid);
    }

    private void scheduleAutoSave(UUID uuid) {
        Map<UUID, Integer> idMap = playerManager.getAutoSaveIdMap();
        if (idMap.get(uuid) != null) {
            server.getScheduler().cancelTask(idMap.remove(uuid));
            //TODO Notification
        }
        Runnable task = new AutoSaveTask(dataInterface, server, uuid);
        int id = scheduler.runTaskTimer(amethyst, task, 3600, 3600).getTaskId();
        idMap.put(uuid, id);
    }
}
