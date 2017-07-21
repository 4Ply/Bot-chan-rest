package com.netply.botchan.web.rest.permissions;

import com.netply.botchan.web.rest.persistence.BaseDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionDatabaseImpl extends BaseDatabase implements PermissionDatabase {
    public PermissionDatabaseImpl(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        super(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Override
    public boolean hasPermission(int clientID, String permission) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT user_id FROM user_permissions WHERE user_id = ? AND permission = ?")) {
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addPermission(int clientID, String permission) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO user_permissions (id, user_id, permission) VALUES (NULL, ?, ?)")) {
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePermission(int clientID, String permission) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM user_permissions WHERE user_id = ? AND permission = ?")) {
            int i = 0;
            preparedStatement.setInt(++i, clientID);
            preparedStatement.setString(++i, permission);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getUsersThatHavePermission(String permission) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT user_id FROM user_permissions WHERE permission = ?")) {
            preparedStatement.setString(1, permission);

            List<Integer> users = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getInt("user_id"));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
