package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageManagerImpl implements MessageManager {
    private static MessageManagerImpl instance;
    private MultiValueMap<Integer, Message> multiValueMap = new LinkedMultiValueMap<>();


    public static MessageManagerImpl getInstance() {
        if (instance == null) {
            instance = new MessageManagerImpl();
        }
        return instance;
    }

    @Override
    public void addMessage(Integer integer, Message message) {
        message.setId(UUID.randomUUID().toString());
        multiValueMap.add(integer, message);
    }

    @Override
    public List<Message> getMessages(Integer integer) {
        List<Message> messages = multiValueMap.get(integer);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    @Override
    public void deleteMessage(Integer clientID, String messageID) {
        multiValueMap.get(clientID).removeIf(message -> messageID.equals(message.getId()));
    }
}
