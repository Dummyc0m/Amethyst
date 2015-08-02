package com.dummyc0m.amethyst.database.task;

import com.dummyc0m.amethyst.database.DataInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Dummyc0m on 7/7/15.
 * Ideally Functional
 */
public class UnlockTask implements Runnable {
    private static final String UNLOCK;

    static {
        UNLOCK = "UPDATE `PlayerData` SET `Online` = 0 WHERE `Player` = ?";
    }

    private final DataInterface dataInterface;
    private final UUID uuid;

    public UnlockTask(DataInterface dataInterface, UUID uuid) {
        this.dataInterface = dataInterface;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            Connection connection = dataInterface.getConnection();
            PreparedStatement unlock = connection.prepareStatement(UNLOCK);
            unlock.setString(1, uuid.toString());
            unlock.executeUpdate();
            unlock.close();
            dataInterface.getPlayerManager().setLoadStage(uuid, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
