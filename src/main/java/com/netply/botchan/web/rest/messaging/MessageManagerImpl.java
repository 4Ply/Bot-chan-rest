package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class MessageManagerImpl implements MessageManager {
    private MultiValueMap<Integer, Message> multiValueMap = new LinkedMultiValueMap<>();


    @Override
    public void addMessage(Integer integer, Message message) {
        multiValueMap.add(integer, message);
    }

    @Override
    public List<Message> getMessages(Integer integer) {
        return multiValueMap.get(integer);
    }
}
