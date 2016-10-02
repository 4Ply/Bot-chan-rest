package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;
import com.netply.botchan.web.rest.persistence.BaseDatabase;
import com.netply.botchan.web.rest.persistence.LoginDatabaseImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TrackedPlayerDatabaseImpl extends BaseDatabase implements TrackedPlayerDatabase {
    private final static Logger LOGGER = Logger.getLogger(LoginDatabaseImpl.class.getName());


    public TrackedPlayerDatabaseImpl(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        super(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Override
    public List<String> getTrackedPlayers(User user) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT playerName FROM tracked_players WHERE clientID = ? AND platform = ?")) {
            preparedStatement.setString(1, user.getClientID());
            preparedStatement.setString(2, user.getPlatform());

            List<String> trackedPlayers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trackedPlayers.add(resultSet.getString("playerName"));
            }
            return trackedPlayers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void addTrackedPlayer(User user, String playerName) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO tracked_players (id, clientID, platform, playerName) VALUES (NULL, ?, ?, ?)")) {
            preparedStatement.setString(1, user.getClientID());
            preparedStatement.setString(2, user.getPlatform());
            preparedStatement.setString(3, playerName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getTrackersForPlayerName(String playerName) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT clientID, platform FROM tracked_players WHERE playerName = ?")) {
            preparedStatement.setString(1, playerName);

            List<User> trackers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trackers.add(new User(resultSet.getString("clientID"), resultSet.getString("platform")));
            }
            return trackers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllTrackedPlayersForPlatform(String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT playerName FROM tracked_players WHERE platform = ?")) {
            preparedStatement.setString(1, platform);

            List<String> trackedPlayers = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trackedPlayers.add(resultSet.getString("playerName"));
            }
            return trackedPlayers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
