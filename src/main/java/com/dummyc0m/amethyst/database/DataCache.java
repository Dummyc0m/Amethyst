package com.dummyc0m.amethyst.database;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dummyc0m on 7/6/15.
 */
public class DataCache {
    public static final String EMPTY = new String();
    public static final String REMOVED = new String();
    private static final DataCache DATA_CACHE = new DataCache();
    private final Map<UUID, String> rawDataMap = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public static DataCache getInstance() {
        return DATA_CACHE;
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
