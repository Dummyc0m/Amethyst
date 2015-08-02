package com.dummyc0m.amethyst.database;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dummyc0m on 7/6/15.
 * Ideally Functional
 */
public class DataCache {
    public static final String EMPTY;

    static {
        EMPTY = new String();
    }

    private final Map<UUID, String> rawDataMap;
    private final Map<UUID, PlayerData> dataMap;

    public DataCache() {
        rawDataMap = new ConcurrentHashMap<>();
        dataMap = new HashMap<>();
    }

    public void cacheRawData(UUID uuid, String data) {
        rawDataMap.put(uuid, data);
    }

    public void cacheData(UUID uuid, PlayerData data) {
        dataMap.put(uuid, data);
    }

    public Map<UUID, String> getRawDataMap() {
        return rawDataMap;
    }

    public Map<UUID, PlayerData> getDataMap() {
        return dataMap;
    }

    public Set<Map.Entry<UUID, String>> getRawEntrySet() {
        return Collections.unmodifiableSet(rawDataMap.entrySet());
    }
}
