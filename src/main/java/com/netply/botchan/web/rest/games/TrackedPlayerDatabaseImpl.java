package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class TrackedPlayerDatabaseImpl implements TrackedPlayerDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public TrackedPlayerDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getTrackedPlayers(User user) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT playerName FROM tracked_players WHERE clientID = ? AND platform = ?");
            preparedStatement.setString(1, user.getClientID());
            preparedStatement.setString(2, user.getPlatform());

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("playerName"));
    }

    @Override
    public void addTrackedPlayer(User user, String playerName) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tracked_players (id, clientID, platform, playerName) VALUES (NULL, ?, ?, ?)");
            preparedStatement.setString(1, user.getClientID());
            preparedStatement.setString(2, user.getPlatform());
            preparedStatement.setString(3, playerName);

            return preparedStatement;
        });
    }

    @Override
    public void removeTrackedPlayer(User user, String playerName) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tracked_players WHERE clientID = ? AND platform = ? AND playerName = ?");
            preparedStatement.setString(1, user.getClientID());
            preparedStatement.setString(2, user.getPlatform());
            preparedStatement.setString(3, playerName);

            return preparedStatement;
        });
    }

    @Override
    public List<User> getTrackersForPlayerName(String playerName) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT clientID, platform FROM tracked_players WHERE playerName = ?");
            preparedStatement.setString(1, playerName);

            return preparedStatement;
        }, (resultSet, i) -> new User(resultSet.getString("clientID"), resultSet.getString("platform")));
    }

    @Override
    public List<String> getAllTrackedPlayersForPlatform(String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT playerName FROM tracked_players WHERE platform = ?");
            preparedStatement.setString(1, platform);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("playerName"));
    }
}
