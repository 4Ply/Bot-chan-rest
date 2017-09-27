package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageManagerImpl implements MessageManager {
    private MessageDatabase messageDatabase;
    private UserManager userManager;


    @Autowired
    public MessageManagerImpl(MessageDatabase messageDatabase, UserManager userManager) {
        this.messageDatabase = messageDatabase;
        this.userManager = userManager;
    }

    @Override
    public void addMessage(Message message) {
        messageDatabase.addMessage(message.getSender(), message.getMessage(), message.getPlatform(), message.isDirect());
    }

    @Override
    public void markMessageAsProcessed(int messageID, String platform) {
        messageDatabase.markMessageAsProcessed(messageID, platform);
    }

    @Override
    public List<Message> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String platform) {
        return messageDatabase.getUnProcessedMessagesForPlatform(messageMatchers, platform);
    }

    @Override
    public void addReply(Reply reply) {
        Message message = messageDatabase.getMessage(reply.getOriginalMessageID());
        messageDatabase.addReply(message.getSender(), reply.getMessage(), message.getPlatform());
    }

    @Override
    public void addDirectMessage(ServerMessage serverMessage) {
        addDirectMessage(serverMessage.getUserID(), serverMessage.getMessage());
    }

    @Override
    public void addDirectMessage(int userID, String message) {
        User user = userManager.getDefaultUser(userID);
        if (user != null) {
            messageDatabase.addReply(user.getClientID(), message, user.getPlatform());
        } else {
            throw new IllegalArgumentException("Invalid User ID");
        }
    }

    @Override
    public void markReplyAsProcessed(int replyID, String platform) {
        messageDatabase.markReplyAsProcessed(replyID, platform);
    }

    @Override
    public List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform) {
        return messageDatabase.getUnProcessedReplies(targetMatchers, platform);
    }
}
