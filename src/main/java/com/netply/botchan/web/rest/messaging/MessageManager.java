package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;

import java.util.ArrayList;
import java.util.List;

public interface MessageManager {
    void addMessage(Message message);

    void markMessageAsProcessed(String messageID, Integer clientID);

    List<Message> getMessagesExcludingOnesDeletedForID(ArrayList<String> messageMatchers, Integer integer);

    void addReply(Reply reply);

    void markReplyAsProcessed(String messageID, Integer clientID);

    List<Reply> getRepliesExcludingOnesDeletedForID(ArrayList<String> targetMatchers, Integer integer);
}
