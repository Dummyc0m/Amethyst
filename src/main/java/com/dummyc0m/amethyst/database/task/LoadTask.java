package com.dummyc0m.amethyst.database.task;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class LoadTask implements Runnable {
    private static final String GET_DATA;
    private static final String INSERT_NEW;
    private static final String SET_ONLINE;

    static {
        //TODO SQL
    }

    private final UUID uuid;

    public LoadTask(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void run() {
        //TODO also check if online and last online > save time, if so the server crashed, which means load!
    }
}
