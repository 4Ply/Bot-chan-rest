package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MessageController {
    private MessageManager messageManager;


    @Autowired
    public MessageController(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @RequestMapping(value = "/messages", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<FromUserMessage> getMessages(@RequestBody MatcherList matcherList) {
        return messageManager.getUnProcessedMessagesForPlatform(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/uniqueMessages", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<FromUserMessage> getMessagesAndMarkThemAsProcessed(@RequestBody MatcherList matcherList) {
        List<FromUserMessage> messageList = messageManager.getUnProcessedMessagesForPlatform(matcherList.getMatchers(), matcherList.getPlatform());
        for (FromUserMessage message : messageList) {
            messageManager.markMessageAsProcessed(message.getId(), matcherList.getPlatform());
        }
        return messageList;
    }

    @RequestMapping(value = "/messagesForUser", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<FromUserMessage> getMessagesForUserUsingPlatform(@RequestBody MatcherList matcherList,
                                                  @RequestParam(value = "clientID") String clientID,
                                                  @RequestParam(value = "platform") String platform) {
        return messageManager.getUnProcessedMessagesForPlatformAndUser(matcherList.getMatchers(), platform, clientID, platform);
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void addMessage(@RequestBody Message message) {
        messageManager.addMessage(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(@RequestParam(value = "platform") String platform,
                              @RequestParam(value = "id") Integer messageID) {
        messageManager.markMessageAsProcessed(messageID, platform);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(@RequestBody Reply reply) {
        messageManager.addReply(reply);
    }

    @RequestMapping(value = "/directMessage", method = RequestMethod.PUT)
    public void addDirectMessage(@RequestBody ServerMessage serverMessage) {
        messageManager.addDirectMessage(serverMessage);
    }

    @RequestMapping(value = "/directReply/{messageID}", method = RequestMethod.PUT)
    public void addDirectMessageForMessageID(@PathVariable("messageID") Integer messageID,
                                             @RequestParam("message") String message) {
        messageManager.addDirectMessageForMessageID(messageID, message);
    }

    @RequestMapping(value = "/directMessage/{id}", method = RequestMethod.GET)
    public String addDirectMessageToUser(@PathVariable(value = "id") Integer id, @RequestParam(value = "message") String message) {
        messageManager.addDirectMessage(id, message);

        return "Message sent";
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getReplies(@RequestBody MatcherList matcherList) {
        return messageManager.getUnProcessedReplies(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/autoDeleteReplies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getRepliesAndMarkThemAsProcessed(@RequestBody MatcherList matcherList) {
        List<ToUserMessage> unProcessedReplies = messageManager.getUnProcessedReplies(matcherList.getMatchers(), matcherList.getPlatform());
        for (ToUserMessage unProcessedReply : unProcessedReplies) {
            messageManager.markReplyAsProcessed(unProcessedReply.getId(), matcherList.getPlatform());
        }
        return unProcessedReplies;
    }

    @RequestMapping(value = "/autoDeleteRepliesForPlatform", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getRepliesAndMarkThemAsProcessedForPlatform(@RequestBody MatcherList matcherList,
                                                                    @RequestParam(value = "clientID") String clientID,
                                                                    @RequestParam(value = "platform") String platform) {
        List<ToUserMessage> unProcessedReplies = messageManager.getUnProcessedReplies(matcherList.getMatchers(), platform)
                .stream().filter(toUserMessage -> toUserMessage.getTarget().equals(clientID)).collect(Collectors.toList());

        for (ToUserMessage unProcessedReply : unProcessedReplies) {
            messageManager.markReplyAsProcessed(unProcessedReply.getId(), platform);
        }
        return unProcessedReplies;
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(@RequestParam(value = "platform") String platform,
                            @RequestParam(value = "id") Integer replyID) {
        messageManager.markReplyAsProcessed(replyID, platform);
    }
}
