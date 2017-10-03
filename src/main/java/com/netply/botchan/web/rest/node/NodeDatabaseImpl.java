package com.netply.botchan.web.rest.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class NodeDatabaseImpl implements NodeDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public NodeDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void ensureNodeExists(String node) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO known_nodes (node_name) VALUES (?) ON DUPLICATE KEY UPDATE last_seen = NOW()");
            int i = 0;
            preparedStatement.setString(++i, node);

            return preparedStatement;
        });
    }

    @Override
    public boolean nodeExists(String node) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT node_name FROM known_nodes WHERE node_name = ? LIMIT 1");
            int i = 0;
            preparedStatement.setString(++i, node);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getBoolean("node_name")).stream().findAny().isPresent();
    }

    @Override
    public boolean isNodeAllowed(int userID, String node) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT enabled FROM user_node_enabled_settings WHERE user_id = ? AND node = ? LIMIT 1");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, node);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getBoolean("enabled")).stream().findFirst().orElse(isDefaultNode(node));
    }

    private boolean isDefaultNode(String node) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT node FROM default_nodes WHERE node = ? LIMIT 1");
            int i = 0;
            preparedStatement.setString(++i, node);

            return preparedStatement;
        }, (resultSet, i) -> 1).stream().findAny().isPresent();
    }

    @Override
    public List<String> listNodes() {
        return jdbcTemplate.query(connection -> connection.prepareStatement("SELECT node_name FROM known_nodes"), (resultSet, i) -> resultSet.getString("node_name"));
    }

    @Override
    public void updateNodeStatus(int userID, String node, Boolean isEnabled) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_node_enabled_settings (user_id, node, enabled) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE enabled = ?");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, node);
            preparedStatement.setBoolean(++i, isEnabled);
            preparedStatement.setBoolean(++i, isEnabled);

            return preparedStatement;
        });
    }
}
