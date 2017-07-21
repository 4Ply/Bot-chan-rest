package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;
import com.netply.botchan.web.rest.persistence.BaseDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabaseImpl extends BaseDatabase implements UserDatabase {
    public UserDatabaseImpl(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        super(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Override
    public int getUserID(String clientID, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT user_id FROM platform_users WHERE client_id = ? AND platform = ? LIMIT 1")) {
            int i = 0;
            preparedStatement.setString(++i, clientID);
            preparedStatement.setString(++i, platform);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int createUser() {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO users (id, name) VALUES (NULL, NULL)")) {
            int i = preparedStatement.executeUpdate();

            if (i >= 1) {
                try (PreparedStatement preparedStatement2 = getConnection().prepareStatement("SELECT LAST_INSERT_ID()")) {
                    ResultSet resultSet = preparedStatement2.executeQuery();

                    if (resultSet.next()) {
                        return resultSet.getInt("LAST_INSERT_ID()");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void setUserID(int userID, String clientID, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO platform_users (id, user_id, client_id, platform) VALUES (NULL, ?, ?, ?) ON DUPLICATE KEY UPDATE user_id = ?")) {
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, clientID);
            preparedStatement.setString(++i, platform);
            preparedStatement.setInt(++i, userID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(int userID) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT client_id, platform FROM platform_users WHERE user_id = ? LIMIT 1")) {
            int i = 0;
            preparedStatement.setInt(++i, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getString("client_id"), resultSet.getString("platform"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
