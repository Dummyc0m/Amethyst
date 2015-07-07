package com.dummyc0m.amethyst.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class PlayerManager {
    private static final PlayerManager PLAYER_MANAGER = new PlayerManager();
    private final Map<UUID, LoadStage> loadStageMap = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoSaveIdMap = new HashMap<>();

    public static PlayerManager getInstance() {
        return PLAYER_MANAGER;
    }

    public void setLoadStage(UUID uuid, LoadStage loadStage) {
        if (loadStage == null) {
            loadStageMap.remove(uuid);
        } else {
            loadStageMap.put(uuid, loadStage);
        }
    }

    public LoadStage getLoadStage(UUID uuid) {
        return loadStageMap.get(uuid);
    }

    public Map<UUID, Integer> getAutoSaveIdMap() {
        return autoSaveIdMap;
    }

    public enum LoadStage {
        JOINED,
        LOADED,
        PROCESSED,
    }
}
