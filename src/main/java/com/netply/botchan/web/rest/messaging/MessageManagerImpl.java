package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageManagerImpl implements MessageManager {
    private static MessageManagerImpl instance;
    private MultiValueMap<Integer, Message> messageMap = new LinkedMultiValueMap<>();
    private MultiValueMap<Integer, Reply> replyMap = new LinkedMultiValueMap<>();


    public static MessageManagerImpl getInstance() {
        if (instance == null) {
            instance = new MessageManagerImpl();
        }
        return instance;
    }

    @Override
    public void addMessage(Integer integer, Message message) {
        message.setId(UUID.randomUUID().toString());
        messageMap.add(integer, message);
    }

    @Override
    public void deleteMessage(Integer clientID, String messageID) {
        messageMap.get(clientID).removeIf(message -> messageID.equals(message.getId()));
    }

    @Override
    public List<Message> getMessages(Integer integer) {
        List<Message> messages = messageMap.get(integer);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    @Override
    public void addReply(Integer integer, Reply reply) {
        reply.setId(UUID.randomUUID().toString());
        replyMap.add(integer, reply);
    }

    @Override
    public void deleteReply(Integer clientID, String replyID) {
        replyMap.get(clientID).removeIf(reply -> replyID.equals(reply.getId()));
    }

    @Override
    public List<Reply> getReplies(Integer integer) {
        List<Reply> messages = replyMap.get(integer);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }
}
