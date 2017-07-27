package com.netply.botchan.web.rest.persistence;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.botchan.web.rest.error.InvalidCredentialsException;
import com.netply.web.security.login.LoginDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

@Deprecated
public class LoginDatabaseImpl extends BaseDatabase implements LoginDatabase {
    private final static Logger LOGGER = Logger.getLogger(LoginDatabaseImpl.class.getName());


    public LoginDatabaseImpl(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        super(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Override
    public BasicResultResponse login(String username, String password) throws InvalidCredentialsException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM `members` WHERE members.username LIKE ? LIMIT 1")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                boolean userLocked = userLocked(userId);
                if (!userLocked && resultSet.getString("password").equals(password)) {
                    return new BasicResultResponse(true, generateSessionKey(userId), userId);
                } else {
                    try (PreparedStatement preparedStatement2 = getConnection().prepareStatement("INSERT INTO login_attempts (id, user_id, time) VALUES (NULL, ?, NOW())")) {
                        preparedStatement2.setInt(1, userId);
                        preparedStatement2.executeUpdate();
                    } catch (SQLException e) {
                        LoginDatabaseImpl.LOGGER.severe(e.getMessage());
                        e.printStackTrace();
                    }
                    if (userLocked) {
                        return new BasicResultResponse("User locked");
                    } else {
                        throw new InvalidCredentialsException();
                    }
                }
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (SQLException e) {
            LoginDatabaseImpl.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }

        return new BasicResultResponse("Server failure");
    }

    @Override
    public String generateSessionKey(int userId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO session_keys (id, user_id, `key`, time) VALUES (NULL, ?, ?, NOW())")) {
            String uuid = UUID.randomUUID().toString();
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();

            return uuid;
        } catch (SQLException e) {
            LoginDatabaseImpl.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkSessionKey(String sessionKey) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM session_keys WHERE time > NOW() - INTERVAL 1 HOUR AND `key` = ? LIMIT 1")) {
            preparedStatement.setString(1, sessionKey);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                try (PreparedStatement preparedStatement2 = getConnection().prepareStatement("UPDATE session_keys SET time = NOW() WHERE id = ?")) {
                    preparedStatement2.setInt(1, resultSet.getInt("id"));
                    preparedStatement2.executeUpdate();
                } catch (SQLException e) {
                    LoginDatabaseImpl.LOGGER.severe(e.getMessage());
                    e.printStackTrace();
                }

                return true;
            }
        } catch (SQLException e) {
            LoginDatabaseImpl.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean isAuthorisedForClientId(String sessionKey, Integer clientId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM session_keys WHERE time > NOW() - INTERVAL 1 HOUR AND `key` = ? AND `user_id` = ? LIMIT 1")) {
            preparedStatement.setString(1, sessionKey);
            preparedStatement.setInt(2, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            LoginDatabaseImpl.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private boolean userLocked(int userId) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM login_attempts WHERE user_id = ? AND time > NOW() - INTERVAL 2 HOUR")) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.last()) {
                if (resultSet.getRow() >= 5) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
