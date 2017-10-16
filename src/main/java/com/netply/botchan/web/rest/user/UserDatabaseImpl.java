package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.rest.error.UnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public int getUserID(int platformID) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM platform_users WHERE id = ? LIMIT 1");
            int i = 0;
            preparedStatement.setInt(++i, platformID);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("user_id")).stream().findFirst().orElse(-1);
    }

    @Override
    public int getPlatformID(String clientID, String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM platform_users WHERE client_id = ? AND platform = ? LIMIT 1");
            int i = 0;
            preparedStatement.setString(++i, clientID);
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("id")).stream().findFirst().orElse(-1);
    }

    @Override
    public List<Integer> getDefaultPlatformIDs(int userID) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM platform_users " +
                    "INNER JOIN default_platforms ON platform_users.id = default_platforms.platform_id AND default_platforms.is_default = TRUE " +
                    "WHERE user_id = ?");
            int i = 0;
            preparedStatement.setInt(++i, userID);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("id"));
    }

    @Override
    public int createUser() {
        int update = jdbcTemplate.update(connection -> connection.prepareStatement("INSERT INTO users (id) VALUES (NULL)"));

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
    public boolean setUserID(int userID, int platformID) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE platform_users SET user_id = ? WHERE id = ?");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setInt(++i, platformID);

            return preparedStatement;
        }) >= 1;
    }

    @Override
    public String getName(int userID) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `name` FROM users WHERE id = ? LIMIT 1");
            int i = 0;
            preparedStatement.setInt(++i, userID);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("name")).stream().findFirst().orElse(null);
    }

    @Override
    public void setName(int userID, String name) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET `name` = ? WHERE id = ?");
            int i = 0;
            preparedStatement.setString(++i, name);
            preparedStatement.setInt(++i, userID);

            return preparedStatement;
        });
    }

    @Override
    public String createPlatformOTP(int platformID) {
        String hash = DigestUtils.md5DigestAsHex(String.valueOf(platformID + "###" + new Date().toString()).getBytes()).substring(0, 7).toUpperCase();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_otps (id, platform_id, `hash`, expiration_date) VALUES (NULL, ?, ?, (NOW() + INTERVAL 10 MINUTE))");
            int i = 0;
            preparedStatement.setInt(++i, platformID);
            preparedStatement.setString(++i, hash);

            return preparedStatement;
        });
        return hash;
    }

    @Override
    public String createUserOTP(int userID, String platformOTP) {
        String hash = DigestUtils.md5DigestAsHex(String.valueOf(userID + "###" + new Date().toString()).getBytes()).substring(0, 7).toUpperCase();
        List<String> hashes = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `hash` FROM user_otps WHERE `hash` = ? AND expiration_date > NOW()");
            int i = 0;
            preparedStatement.setString(++i, platformOTP);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("hash"));

        if (hashes.isEmpty()) {
            throw new UnauthorisedException("Platform OTP does not exist or was already consumed");
        }
        hashes.forEach(consumedOTP -> linkPlatformOTPToUser(userID, hash, consumedOTP));
        return hash;
    }

    private void linkPlatformOTPToUser(int userID, String hash, String platformOTP) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_authorised_otps (id, user_id, platform_otp, `hash`, expiration_date) VALUES (NULL, ?, ?, ?, (NOW() + INTERVAL 10 MINUTE))");
            int i = 0;
            preparedStatement.setInt(++i, userID);
            preparedStatement.setString(++i, platformOTP);
            preparedStatement.setString(++i, hash);

            return preparedStatement;
        });
    }

    @Override
    public String getPlatformOTP(int platformID) {
        Optional<String> platformOTP = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `hash` FROM user_otps WHERE `platform_id` = ? AND expiration_date > NOW()");
            int i = 0;
            preparedStatement.setInt(++i, platformID);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getString("hash")).stream().findFirst();

        if (platformOTP.isPresent()) {
            return platformOTP.get();
        }
        throw new UnauthorisedException("Platform OTP does not exist or was already consumed");
    }

    @Override
    public int getUserIDForOTP(String platformOTP, String userOTP) {
        Optional<Integer> userID = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `user_id` FROM user_authorised_otps WHERE `platform_otp` = ? AND `hash` = ? AND expiration_date > NOW()");
            int i = 0;
            preparedStatement.setString(++i, platformOTP);
            preparedStatement.setString(++i, userOTP);

            return preparedStatement;
        }, (resultSet, i) -> resultSet.getInt("user_id")).stream().findFirst();

        if (userID.isPresent()) {
            return userID.get();
        }
        throw new UnauthorisedException("Platform OTP does not exist or was already consumed");
    }

    @Override
    public void invalidatePlatformOTP(String platformOTP) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_otps SET expiration_date = (NOW() - INTERVAL 10 MINUTE) WHERE `hash` = ?");
            int i = 0;
            preparedStatement.setString(++i, platformOTP);

            return preparedStatement;
        });
    }

    @Override
    public void invalidateAuthorisedOTP(String userOTP) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_authorised_otps SET expiration_date = (NOW() - INTERVAL 10 MINUTE) WHERE `hash` = ?");
            int i = 0;
            preparedStatement.setString(++i, userOTP);

            return preparedStatement;
        });
    }
}
