package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.FromUserMessage;
import com.netply.botchan.web.model.ToUserMessage;

import java.util.List;

public interface MessageDatabase {
    FromUserMessage getMessage(int messageID);

    void addMessage(int platformID, String message, boolean direct);

    void markMessageAsProcessed(int messageID, String platform);

    List<FromUserMessage> getUnProcessedMessagesForPlatform(String platform);

    void addReply(String node, int platformID, String message);

    void markReplyAsProcessed(int replyID, String platform);

    List<ToUserMessage> getUnProcessedReplies(List<String> targetMatchers, String platform);
}
