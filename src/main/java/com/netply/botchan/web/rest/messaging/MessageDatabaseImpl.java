package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.FromUserMessage;
import com.netply.botchan.web.model.ToUserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class MessageDatabaseImpl implements MessageDatabase {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public MessageDatabaseImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FromUserMessage getMessage(int messageID) {
        Optional<FromUserMessage> messageOptional = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, platform_id, message, direct FROM messages WHERE id = ?");
            preparedStatement.setInt(1, messageID);
            return preparedStatement;
        }, MessageDatabaseImpl::createMessage).stream().findFirst();

        return messageOptional.orElse(null);
    }

    private static FromUserMessage createMessage(ResultSet resultSet, int i) throws SQLException {
        return new FromUserMessage(resultSet.getInt("id"), resultSet.getString("message"), resultSet.getInt("platform_id"), resultSet.getBoolean("direct"));
    }

    @Override
    public void addMessage(int platformID, String message, boolean direct) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (id, platform_id, message, direct) VALUES (NULL, ?, ?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, platformID);
            preparedStatement.setString(++i, message);
            preparedStatement.setBoolean(++i, direct);

            return preparedStatement;
        });
    }

    @Override
    public void markMessageAsProcessed(int messageID, String platform) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO message_processed (message_id, platform) VALUES (?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, messageID);
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        });
    }

    @Override
    public List<FromUserMessage> getUnProcessedMessagesForPlatform(String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, platform_id, message, direct FROM messages " +
                    "WHERE messages.id NOT IN (SELECT message_id FROM message_processed WHERE message_processed.platform = ?)");
            int i = 0;
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, MessageDatabaseImpl::createMessage);
    }

    @Override
    public void addReply(int platformID, String message) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO replies (id, platform_id, message) VALUES (NULL, ?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, platformID);
            preparedStatement.setString(++i, message);

            return preparedStatement;
        });
    }

    @Override
    public void markReplyAsProcessed(int replyID, String platform) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO replies_processed (reply_id, platform) VALUES (?, ?)");
            int i = 0;
            preparedStatement.setInt(++i, replyID);
            preparedStatement.setString(++i, platform);
            return preparedStatement;
        });
    }

    @Override
    @Transactional
    public List<ToUserMessage> getUnProcessedReplies(List<String> targetMatchers, String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT replies.id, platform_users.client_id, message FROM replies " +
                    "INNER JOIN platform_users ON replies.platform_id = platform_users.id " +
                    "WHERE replies.id NOT IN (SELECT reply_id FROM replies_processed WHERE replies_processed.platform = ?) " +
                    "AND platform_users.platform = ?");
            int i = 0;
            preparedStatement.setString(++i, platform);
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, (resultSet, i) -> new ToUserMessage(resultSet.getInt("id"), resultSet.getString("client_id"), resultSet.getString("message")));
    }
}
