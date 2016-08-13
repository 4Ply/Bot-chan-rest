package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;

import java.util.List;

public interface MessageManager {
    void addMessage(Integer integer, Message message);

    List<Message> getMessages(Integer integer);
}
