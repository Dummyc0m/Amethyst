package com.dummyc0m.amethyst.database;

import com.dummyc0m.amethystcore.database.ACDatabase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dummyc0m on 5/10/15.
 */
public class DataInterface extends ACDatabase {
    private static DataInterface DATA_INTERFACE;
    private final DataCache cache = DataCache.getInstance();
    private final ExecutorService service = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());

    public DataInterface(String type, String hostname, int port, String database, String username, String password) {
        super(type, hostname, port, database, username, password);
        DATA_INTERFACE = this;
    }

    public static DataInterface getInstance() {
        return DATA_INTERFACE;
    }

    public void queueLoad(UUID uuid) {
        //TODO
    }

    public void queueSave(UUID uuid, boolean unlock) {
        //TODO
    }

    public void terminateSave(List<UUID> uuids, boolean unlock) {
        //TODO
        try {
            service.awaitTermination(100000000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
