package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.ToUserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public Message getMessage(int messageID) {
        Optional<Message> messageOptional = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, sender, message, platform, direct FROM messages WHERE id = ?");
            preparedStatement.setInt(1, messageID);
            return preparedStatement;
        }, MessageDatabaseImpl::createMessage).stream().findFirst();

        return messageOptional.orElse(null);
    }

    private static Message createMessage(ResultSet resultSet, int i) throws SQLException {
        return new Message(resultSet.getInt("id"), resultSet.getString("message"), resultSet.getString("sender"), resultSet.getString("platform"), resultSet.getBoolean("direct"));
    }

    @Override
    public void addMessage(String sender, String message, String platform, boolean direct) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (id, sender, message, platform, direct) VALUES (NULL, ?, ?, ?, ?)");
            int i = 0;
            preparedStatement.setString(++i, sender);
            preparedStatement.setString(++i, message);
            preparedStatement.setString(++i, platform);
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
    public List<Message> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, sender, message, messages.platform, direct FROM messages " +
                    "LEFT JOIN message_processed ON messages.id = message_processed.message_id " +
                    "WHERE messages.id NOT IN (SELECT message_id FROM message_processed WHERE message_processed.platform = ?)");
            int i = 0;
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, MessageDatabaseImpl::createMessage);
    }

    @Override
    public void addReply(String target, String message, String platform) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO replies (id, target, message, platform) VALUES (NULL, ?, ?, ?)");
            int i = 0;
            preparedStatement.setString(++i, target);
            preparedStatement.setString(++i, message);
            preparedStatement.setString(++i, platform);

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
    public List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, target, message FROM replies " +
                    "LEFT JOIN replies_processed ON replies.id = replies_processed.reply_id " +
                    "WHERE replies.id NOT IN (SELECT reply_id FROM replies_processed WHERE replies_processed.platform = ?)");
            int i = 0;
            preparedStatement.setString(++i, platform);

            return preparedStatement;
        }, (resultSet, i) -> new ToUserMessage(resultSet.getInt("id"), resultSet.getString("target"), resultSet.getString("message")));
    }
}
