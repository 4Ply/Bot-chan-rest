package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.ToUserMessage;

import java.util.ArrayList;
import java.util.List;

public interface MessageDatabase {
    Message getMessage(int messageID);

    void addMessage(String sender, String message, String platform, boolean direct);

    void markMessageAsProcessed(int messageID, String platform);

    List<Message> getUnProcessedMessagesForPlatform(String platform);

    void addReply(String target, String message, String platform);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(ArrayList<String> targetMatchers, String platform);
}
