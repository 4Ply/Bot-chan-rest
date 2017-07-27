package com.netply.botchan.web.rest.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class PermissionDatabaseImpl implements PermissionDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public PermissionDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean hasPermission(int clientID, String permission) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM user_permissions WHERE user_id = ? AND permission = ?");
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);

            return preparedStatement;
        }, (RowMapper<Boolean>) (resultSet, i) -> null).size() > 0;
    }

    @Override
    public void addPermission(int clientID, String permission) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_permissions (id, user_id, permission) VALUES (NULL, ?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);
            return preparedStatement;
        });
    }

    @Override
    public void removePermission(int clientID, String permission) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_permissions WHERE user_id = ? AND permission = ?");
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);
            return preparedStatement;
        });
    }

    @Override
    public List<Integer> getUsersThatHavePermission(String permission) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM user_permissions WHERE permission = ?");
            preparedStatement.setString(1, permission);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("user_id"));
    }
}
