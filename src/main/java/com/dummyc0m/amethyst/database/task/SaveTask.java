package com.dummyc0m.amethyst.database.task;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class SaveTask implements Runnable {
    private static final String SAVE;

    static {
        //TODO SQL
    }

    private final Map<UUID, String> dataMap;
    private final int lock;

    public SaveTask(UUID uuid, String data, boolean unlock) {
        this.dataMap = ImmutableMap.of(uuid, data);
        this.lock = unlock ? 0 : 1;
    }

    public SaveTask(Map<UUID, String> dataMap, boolean unlock) {
        this.dataMap = dataMap;
        this.lock = unlock ? 0 : 1;
    }


    @Override
    public void run() {
        //TODO
    }
}
