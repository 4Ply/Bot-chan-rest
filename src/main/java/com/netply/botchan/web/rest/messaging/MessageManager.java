package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.botchan.web.rest.error.TokenNotFoundException;

import java.util.List;

public interface MessageManager {
    void addMessage(String platform, Message message);

    void markMessageAsProcessed(int messageID, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatform(List<String> messageMatchers, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatformAndUser(List<String> messageMatchers, String node, String clientID, String platform);

    void addReply(String node, Reply reply);

    void addDirectMessage(String node, ServerMessage serverMessage);

    void addDirectMessage(String node, int userID, String message);

    void addDirectMessage(String node, String token, String message) throws TokenNotFoundException;

    void addDirectMessageForMessageID(String node, int messageID, String message);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(List<String> targetMatchers, String platform);
}
