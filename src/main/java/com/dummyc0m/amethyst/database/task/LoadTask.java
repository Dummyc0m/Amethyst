package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.database.DataCache;
import com.dummyc0m.amethyst.database.DataInterface;
import com.dummyc0m.amethyst.database.PlayerManager;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 * Ideally Functional
 */
public class LoadTask implements Runnable {
    private static final String GET_DATA;
    private static final String INSERT_NEW;
    private static final String SET_ONLINE;

    static {
        GET_DATA = "SELECT `Data`,`Online`,`Last` FROM `PlayerData` WHERE `Player` = ?";
        INSERT_NEW = "INSERT INTO `PlayerData`(`Player`,`Online`) VALUES(?,1)";
        SET_ONLINE = "UPDATE `PlayerData` SET `Online` = 1 WHERE `Player` = ?";
    }

    private final UUID uuid;
    private final DataInterface dataInterface;
    private final Server server;


    public LoadTask(DataInterface dataInterface, UUID uuid) {
        this.dataInterface = dataInterface;
        this.server = dataInterface.getServer();
        this.uuid = uuid;
    }

    @Override
    public void run() {
        if (!load()) {
            server.getScheduler().runTask(dataInterface.getAmethyst(), new KickTask(server.getPlayer(uuid)));
        }
    }

    private boolean load() {
        try {
            Connection connection = dataInterface.getConnection();
            PreparedStatement getData = connection.prepareStatement(GET_DATA);
            getData.setString(1, uuid.toString());
            ResultSet resultSet = getData.executeQuery();
            if (!resultSet.next()) {
                createPlayer();
                processData(DataCache.EMPTY);
            } else if (resultSet.getInt(2) == 0) {
                setOnline();
                processData(resultSet.getString(1));
            } else if (resultSet.getLong(3) != 0 && (System.currentTimeMillis() - resultSet.getLong(3)) > 300000) {
                // If locked and not being saved for 5 minutes, then assume player logged off forcibly
                String data = resultSet.getString(1);
                processData(data != null ? data : DataCache.EMPTY);
            } else {
                getData.close();
                return false;
            }
            getData.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setOnline() throws SQLException {
        Connection connection = dataInterface.getConnection();
        PreparedStatement setOnline = connection.prepareStatement(SET_ONLINE);
        setOnline.setString(1, uuid.toString());
        setOnline.executeUpdate();
        setOnline.close();
    }

    private void createPlayer() throws SQLException {
        Connection connection = dataInterface.getConnection();
        PreparedStatement insertPlayer = connection.prepareStatement(INSERT_NEW);
        insertPlayer.setString(1, uuid.toString());
        insertPlayer.executeUpdate();
        insertPlayer.close();
    }

    private void processData(String data) {
        //LOADED STAGE
        dataInterface.getPlayerManager().setLoadStage(uuid, PlayerManager.LoadStage.LOADED);
        dataInterface.getCache().cacheRawData(uuid, data);
    }

    public class KickTask implements Runnable {
        private final Player player;

        protected KickTask(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            //TODO Message Configuration
            player.kickPlayer("");
        }
    }
}
