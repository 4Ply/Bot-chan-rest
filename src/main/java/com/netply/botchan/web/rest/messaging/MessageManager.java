package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;

import java.util.ArrayList;
import java.util.List;

public interface MessageManager {
    void addMessage(Message message);

    void markMessageAsProcessed(int messageID, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatformAndUser(ArrayList<String> messageMatchers, String node, String clientID, String platform);

    void addReply(Reply reply);

    void addDirectMessage(ServerMessage serverMessage);

    void addDirectMessage(int userID, String message);

    void addDirectMessageForMessageID(Integer messageID, String message);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform);
}
