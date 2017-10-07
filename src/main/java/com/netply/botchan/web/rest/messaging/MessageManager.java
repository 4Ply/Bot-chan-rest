package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
import com.netply.botchan.web.model.ServerMessage;
import com.netply.botchan.web.model.ToUserMessage;

import java.util.ArrayList;
import java.util.List;

public interface MessageManager {
    void addMessage(Message message);

    void markMessageAsProcessed(int messageID, String platform);

    List<Message> getUnProcessedMessagesForPlatform(ArrayList<String> messageMatchers, String platform);

    List<Message> getUnProcessedMessagesForPlatformAndUser(ArrayList<String> messageMatchers, String node, String clientID, String platform);

    void addReply(Reply reply);

    void addDirectMessage(ServerMessage serverMessage);

    void addDirectMessage(int userID, String message);

    void addDirectMessageForMessageID(Integer messageID, String message);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform);
}
