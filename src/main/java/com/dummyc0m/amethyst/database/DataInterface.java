package com.dummyc0m.amethyst.database;

import com.dummyc0m.amethyst.Amethyst;
import com.dummyc0m.amethyst.database.task.LoadTask;
import com.dummyc0m.amethyst.database.task.SaveTask;
import com.dummyc0m.amethyst.database.task.UnlockTask;
import com.dummyc0m.amethystcore.database.ACDatabase;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dummyc0m on 5/10/15.
 * Ideally functional
 */
public class DataInterface extends ACDatabase {
    private final Amethyst amethyst;
    private final Server server;
    private final DataConverter dataConverter;
    private final DataCache cache;
    private final PlayerManager playerManager;
    private final ExecutorService service;

    public DataInterface(String type, String hostname, int port, String database, String username, String password, Amethyst amethyst) {
        super(type, hostname, port, database, username, password);
        this.amethyst = amethyst;
        server = amethyst.getServer();
        cache = new DataCache();
        dataConverter = new DataConverter(this, cache, server);
        playerManager = new PlayerManager();
        service = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60000, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS AmethystData("
                    + "`Id` int NOT NULL AUTO_INCREMENT, "
                    + "`Player` char(36) NULL, "
                    + "`Data` text NULL, "
                    + "`OnlineLock` int NULL, "
                    + "`Last` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY(`Id`), "
                    + "INDEX `player_index` (`Player`));";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Amethyst getAmethyst() {
        return amethyst;
    }

    public Server getServer() {
        return server;
    }

    public DataCache getCache() {
        return cache;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DataConverter getDataConverter() {
        return dataConverter;
    }

    public void queueLoad(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player is null");
        } else if (!player.isOnline()) {
            // Player is gone
            playerManager.setLoadStage(player.getUniqueId(), null);
        } else {
            service.execute(new LoadTask(this, player.getUniqueId()));
        }
    }

    public void queueSave(Player player, boolean unlock) {
        if (player == null) {
            throw new IllegalArgumentException("Player is null");
        }
        UUID uuid = player.getUniqueId();
        service.execute(new SaveTask(this, uuid, dataConverter.serializeData(server.getPlayer(uuid)), unlock));
    }

    public void queueUnlock(UUID uuid) {
        service.execute(new UnlockTask(this, uuid));
    }

    public void terminateSave(List<UUID> uuids, boolean unlock) {
        //TODO
        try {
            service.awaitTermination(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    Player p = //The player you want to connect to another server
//
//            ByteArrayOutputStream b = new ByteArrayOutputStream();
//
//    DataOutputStream out = new DataOutputStream(b);
//
//    try {
//
//        out.writeUTF("Connect");
//
//        out.writeUTF("lobby"); // Name of the server to connect to
//
//    } catch (IOException eee) { }
//
//    p.sendPluginMessage(pbgame.plugin, "BungeeCord", b.toByteArray());
//
//    You also have to register the outgoing plugin channel, like so:
//
//    Code (Java):
//    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
}
