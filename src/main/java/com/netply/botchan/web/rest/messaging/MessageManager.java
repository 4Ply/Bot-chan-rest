package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;

import java.util.List;

public interface MessageManager {
    void addMessage(Integer integer, Message message);

    void deleteMessage(Integer clientID, String messageID);

    List<Message> getMessages(Integer integer);

    void addReply(Integer integer, Reply reply);

    void deleteReply(Integer clientID, String messageID);

    List<Reply> getReplies(Integer integer);
}
