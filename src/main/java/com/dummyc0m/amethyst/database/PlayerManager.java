package com.dummyc0m.amethyst.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dummyc0m on 7/7/15.
 * ideally functional
 */
public class PlayerManager {
    private final Map<UUID, LoadStage> loadStageMap;
    private final Map<UUID, Integer> autoSaveIdMap;

    public PlayerManager() {
        loadStageMap = new ConcurrentHashMap<>();
        autoSaveIdMap = new HashMap<>();
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
        CONNECTED,
        JOINED,
        LOADED,
    }
}
