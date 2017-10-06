package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.botchan.web.rest.node.NodeManager;
import com.netply.botchan.web.rest.user.PlatformUser;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageManagerImpl implements MessageManager {
    private MessageDatabase messageDatabase;
    private UserManager userManager;
    private NodeManager nodeManager;


    @Autowired
    public MessageManagerImpl(MessageDatabase messageDatabase, UserManager userManager, NodeManager nodeManager) {
        this.messageDatabase = messageDatabase;
        this.userManager = userManager;
        this.nodeManager = nodeManager;
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
    public List<Message> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String node) {
        nodeManager.ensureNodeExists(node);
        List<Message> messageList = messageDatabase.getUnProcessedMessagesForPlatform(messageMatchers, node);

        List<PlatformUser> platformUsers = getPlatformUsersWhereNodeIsAuthorised(node, messageList);

        return messageList.stream()
                .filter(message -> platformUsers.contains(new PlatformUser(message.getSender(), message.getPlatform())))
                .collect(Collectors.toList());
    }

    private List<PlatformUser> getPlatformUsersWhereNodeIsAuthorised(String node, List<Message> messageList) {
        return messageList.stream()
                .map(message -> new PlatformUser(message.getSender(), message.getPlatform()))
                .distinct()
                .filter(platformUser -> nodeManager.isNodeAllowed(platformUser.getClientID(), platformUser.getPlatform(), node))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getUnProcessedMessagesForPlatformAndUser(ArrayList<String> messageMatchers, String node, String clientID, String platform) {
        int userID = userManager.getUserID(clientID, platform);
        nodeManager.ensureNodeExists(node);
        List<Message> messageList = messageDatabase.getUnProcessedMessagesForPlatform(messageMatchers, node);

        List<PlatformUser> platformUsers = getPlatformUsersWhereNodeIsAuthorised(node, messageList);

        List<PlatformUser> targetPlatformUsers = platformUsers.stream()
                .filter(platformUser -> userManager.getUserID(platformUser.getClientID(), platformUser.getPlatform()) == userID)
                .collect(Collectors.toList());

        return messageList.stream()
                .filter(message -> targetPlatformUsers.contains(new PlatformUser(message.getSender(), message.getPlatform())))
                .collect(Collectors.toList());
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
