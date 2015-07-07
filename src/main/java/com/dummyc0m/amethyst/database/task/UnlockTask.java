package com.dummyc0m.amethyst.database.task;

import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 */
public class UnlockTask implements Runnable {
    private static final String UNLOCK;

    static {
        UNLOCK = "";
    }

    private final UUID uuid;

    public UnlockTask(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void run() {
        //TODO
    }
}
