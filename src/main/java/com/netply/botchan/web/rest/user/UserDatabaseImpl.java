package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class UserDatabaseImpl implements UserDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public UserDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getUserID(String clientID, String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM platform_users WHERE client_id = ? AND platform = ? LIMIT 1");
            int i = 0;
            preparedStatement.setString(++i, clientID);
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("user_id")).stream().findFirst().orElse(-1);
    }

    @Override
    public int createUser() {
        int update = jdbcTemplate.update(connection -> connection.prepareStatement("INSERT INTO users (id, name) VALUES (NULL, NULL)"));

        if (update >= 1) {
            return jdbcTemplate.query(connection -> connection.prepareStatement("SELECT LAST_INSERT_ID()"), (resultSet, i) -> resultSet.getInt("LAST_INSERT_ID()")).stream().findFirst().orElse(-1);
        }
        return -1;
    }

    @Override
    public boolean setUserID(int userID, String clientID, String platform) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO platform_users (id, user_id, client_id, platform) VALUES (NULL, ?, ?, ?) ON DUPLICATE KEY UPDATE user_id = ?");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, clientID);
            preparedStatement.setString(++i, platform);
            preparedStatement.setInt(++i, userID);

            return preparedStatement;
        }) >= 1;
    }

    @Override
    public User getUser(int userID) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_id, platform FROM platform_users WHERE user_id = ? LIMIT 1");
            int i = 0;
            preparedStatement.setInt(++i, userID);

            return preparedStatement;
        }, (resultSet, i) -> new User(resultSet.getString("client_id"), resultSet.getString("platform"))).stream().findFirst().orElse(null);
    }
}
