package com.netply.botchan.web.rest.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccessTokenDatabaseImpl implements AccessTokenDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public AccessTokenDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Integer> getUserID(String token) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM user_direct_message_tokens WHERE token = ? LIMIT 1");
            preparedStatement.setString(1, token);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("user_id")).stream().findFirst();
    }

    @Override
    public String generateToken(int userID) {
        String token = UUID.randomUUID().toString();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_direct_message_tokens (user_id, token) VALUES (?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, token);

            return preparedStatement;
        });
        return token;
    }

    @Override
    public List<String> listTokens(int userID) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM user_direct_message_tokens WHERE user_id = ?");
            preparedStatement.setInt(1, userID);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("token"));
    }
}
