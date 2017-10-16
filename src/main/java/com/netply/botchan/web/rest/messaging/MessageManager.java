package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;

import java.util.List;

public interface MessageManager {
    void addMessage(String platform, Message message);

    void markMessageAsProcessed(int messageID, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatform(List<String> messageMatchers, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatformAndUser(List<String> messageMatchers, String node, String clientID, String platform);

    void addReply(Reply reply);

    void addDirectMessage(ServerMessage serverMessage);

    void addDirectMessage(int userID, String message);

    void addDirectMessageForMessageID(int messageID, String message);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(List<String> targetMatchers, String platform);
}
