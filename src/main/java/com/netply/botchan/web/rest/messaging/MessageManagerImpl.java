package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.botchan.web.rest.token.AccessTokenManager;
import com.netply.botchan.web.rest.error.TokenNotFoundException;
import com.netply.botchan.web.rest.node.NodeManager;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageManagerImpl implements MessageManager {
    private MessageDatabase messageDatabase;
    private UserManager userManager;
    private NodeManager nodeManager;
    private AccessTokenManager accessTokenManager;


    @Autowired
    public MessageManagerImpl(MessageDatabase messageDatabase, UserManager userManager, NodeManager nodeManager, AccessTokenManager accessTokenManager) {
        this.messageDatabase = messageDatabase;
        this.userManager = userManager;
        this.nodeManager = nodeManager;
        this.accessTokenManager = accessTokenManager;
    }

    @Override
    public void addMessage(String platform, Message message) {
        messageDatabase.addMessage(userManager.getPlatformID(message.getSender(), platform), message.getMessage(), message.isDirect());
    }

    @Override
    public void markMessageAsProcessed(int messageID, String platform) {
        messageDatabase.markMessageAsProcessed(messageID, platform);
    }

    @Override
    public List<FromUserMessage> getUnProcessedMessagesForPlatform(List<String> messageMatchers, String node, String secondsBeforeNow) {
        nodeManager.ensureNodeExists(node);
        List<FromUserMessage> messageList = messageDatabase.getUnProcessedMessagesForPlatform(secondsBeforeNow, node);

        List<Integer> platformIDs = getPlatformUsersWhereNodeIsAuthorised(messageList, node);

        return messagesMatchingMatcherForPlatformUser(messageList, messageMatchers, platformIDs);
    }

    @Override
    public List<FromUserMessage> getUnProcessedMessagesForPlatformAndUser(List<String> messageMatchers, String node, String secondsBeforeNow, String clientID, String platform) {
        int userID = userManager.getUserID(clientID, platform);
        nodeManager.ensureNodeExists(node);
        List<FromUserMessage> messageList = messageDatabase.getUnProcessedMessagesForPlatform(secondsBeforeNow, node);

        List<Integer> platformIDs = getPlatformUsersWhereNodeIsAuthorised(messageList, node);

        List<Integer> targetPlatformIDs = platformIDs.stream()
                .filter(platformID -> userManager.getUserID(platformID) == userID)
                .collect(Collectors.toList());

        return messagesMatchingMatcherForPlatformUser(messageList, messageMatchers, targetPlatformIDs);
    }

    private List<Integer> getPlatformUsersWhereNodeIsAuthorised(List<FromUserMessage> messageList, String node) {
        return messageList.stream()
                .map(FromUserMessage::getPlatformID)
                .distinct()
                .filter(platformID -> nodeManager.isNodeAllowedForPlatformID(platformID, node))
                .collect(Collectors.toList());
    }

    private List<FromUserMessage> messagesMatchingMatcherForPlatformUser(List<FromUserMessage> messageList, List<String> messageMatchers, List<Integer> platformIDs) {
        return messageList.stream()
                .filter(message -> messageMatchers.stream().anyMatch(s -> message.getMessage().matches(s)))
                .filter(message -> platformIDs.contains(message.getPlatformID()))
                .collect(Collectors.toList());
    }

    @Override
    public void addReply(String node, Reply reply) {
        FromUserMessage message = messageDatabase.getMessage(reply.getOriginalMessageID());
        messageDatabase.addReply(node, message.getPlatformID(), reply.getMessage());
    }

    @Override
    public void addDirectMessage(String node, ServerMessage serverMessage) {
        addDirectMessage(node, serverMessage.getUserID(), serverMessage.getMessage());
    }

    @Override
    public void addDirectMessage(String node, String token, String message) throws TokenNotFoundException {
        int userID = accessTokenManager.getUserID(token);

        addDirectMessage(node, userID, message);
    }

    @Override
    public void addDirectMessage(String node, int userID, String message) {
        List<Integer> platformIDs = userManager.getDefaultPlatformIDs(userID);
        for (Integer platformID : platformIDs) {
            messageDatabase.addReply(node, platformID, message);
        }
    }

    @Override
    public void addDirectMessageForMessageID(String node, int messageID, String message) {
        FromUserMessage originalMessage = messageDatabase.getMessage(messageID);
        messageDatabase.addReply(node, originalMessage.getPlatformID(), message);
    }

    @Override
    public void markReplyAsProcessed(int replyID, String platform) {
        messageDatabase.markReplyAsProcessed(replyID, platform);
    }

    @Override
    public List<ToUserMessage> getUnProcessedReplies(List<String> targetMatchers, String platform) {
        return messageDatabase.getUnProcessedReplies(targetMatchers, platform);
    }
}
