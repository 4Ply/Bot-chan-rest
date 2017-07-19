package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.ToUserMessage;
import com.netply.botchan.web.rest.persistence.BaseDatabase;
import com.netply.botchan.web.rest.persistence.LoginDatabaseImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MessageDatabaseImpl extends BaseDatabase implements MessageDatabase {
    private final static Logger LOGGER = Logger.getLogger(LoginDatabaseImpl.class.getName());


    public MessageDatabaseImpl(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        super(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Override
    public Message getMessage(int messageID) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT id, sender, message, platform, direct FROM messages WHERE id = ?")) {
            preparedStatement.setInt(1, messageID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Message(resultSet.getInt("id"), resultSet.getString("message"), resultSet.getString("sender"), resultSet.getString("platform"), resultSet.getBoolean("direct"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addMessage(String sender, String message, String platform, boolean direct) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO messages (id, sender, message, platform, direct) VALUES (NULL, ?, ?, ?, ?)")) {
            int i = 0;
            preparedStatement.setString(++i, sender);
            preparedStatement.setString(++i, message);
            preparedStatement.setString(++i, platform);
            preparedStatement.setBoolean(++i, direct);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markMessageAsProcessed(int messageID, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO message_processed (message_id, platform) VALUES (?, ?)")) {
            int i = 0;
            preparedStatement.setInt(++i, messageID);
            preparedStatement.setString(++i, platform);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT id, sender, message, messages.platform, direct FROM messages " +
                "LEFT JOIN message_processed ON messages.id = message_processed.message_id " +
                "WHERE messages.id NOT IN (SELECT message_id FROM message_processed WHERE message_processed.platform = ?)")) {
            int i = 0;
            preparedStatement.setString(++i, platform);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(new Message(resultSet.getInt("id"), resultSet.getString("message"), resultSet.getString("sender"), resultSet.getString("platform"), resultSet.getBoolean("direct")));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void addReply(String target, String message, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO replies (id, target, message, platform) VALUES (NULL, ?, ?, ?)")) {
            int i = 0;
            preparedStatement.setString(++i, target);
            preparedStatement.setString(++i, message);
            preparedStatement.setString(++i, platform);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markReplyAsProcessed(int replyID, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO replies_processed (reply_id, platform) VALUES (?, ?)")) {
            int i = 0;
            preparedStatement.setInt(++i, replyID);
            preparedStatement.setString(++i, platform);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT id, target, message FROM replies " +
                "LEFT JOIN replies_processed ON replies.id = replies_processed.reply_id " +
                "WHERE replies.id NOT IN (SELECT reply_id FROM replies_processed WHERE replies_processed.platform = ?)")) {
            int i = 0;
            preparedStatement.setString(++i, platform);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ToUserMessage> toUserMessages = new ArrayList<>();
            while (resultSet.next()) {
                toUserMessages.add(new ToUserMessage(resultSet.getInt("id"), resultSet.getString("target"), resultSet.getString("message")));
            }
            return toUserMessages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
