package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageManagerImpl implements MessageManager {
    private static MessageManagerImpl instance;
    private MultiValueMap<Message, Integer> messageMap = new LinkedMultiValueMap<>();
    private MultiValueMap<Reply, Integer> replyMap = new LinkedMultiValueMap<>();


    @Deprecated // Replace with DB
    public static MessageManagerImpl getInstance() {
        if (instance == null) {
            instance = new MessageManagerImpl();
        }
        return instance;
    }

    @Override
    public void addMessage(Message message) {
        message.setId(UUID.randomUUID().toString());
        messageMap.add(message, -1);
    }

    @Override
    public void markMessageAsProcessed(String messageID, Integer clientID) {
        messageMap.keySet().stream()
                .filter(message -> messageID.equals(message.getId()))
                .distinct()
                .forEach(message -> messageMap.add(message, clientID));
    }

    @Override
    public List<Message> getMessagesExcludingOnesDeletedForID(ArrayList<String> messageMatchers, Integer integer) {
        return messageMap.keySet().stream()
                .distinct()
                .filter(doesMessageMatchAnyMessageMatcherPattern(messageMatchers))
                .filter(message -> !messageMap.get(message).contains(integer))
                .collect(Collectors.toList());
    }

    private Predicate<Message> doesMessageMatchAnyMessageMatcherPattern(ArrayList<String> messageMatchers) {
        return message -> {
            for (String messageMatcher : messageMatchers) {
                if (message.getMessage().matches(messageMatcher)) {
                    return true;
                }
            }
            return false;
        };
    }

    @Override
    public void addReply(Reply reply) {
        reply.setId(UUID.randomUUID().toString());
        replyMap.add(reply, -1);
    }

    @Override
    public void markReplyAsProcessed(String replyID, Integer clientID) {
        replyMap.keySet().stream()
                .filter(reply -> replyID.equals(reply.getId()))
                .distinct()
                .forEach(message -> replyMap.add(message, clientID));
    }

    @Override
    public List<Reply> getRepliesExcludingOnesDeletedForID(ArrayList<String> platformMatchers, Integer integer) {
        return replyMap.keySet().stream()
                .distinct()
                .filter(reply -> platformMatchers.contains(reply.getPlatform()))
                .filter(reply -> !replyMap.get(reply).contains(integer))
                .collect(Collectors.toList());
    }
}
