package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.botchan.web.rest.error.TokenNotFoundException;
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
    List<FromUserMessage> getMessages(@RequestHeader(value = "X-Consumer-Username") String platform,
                                      @RequestParam(value = "secondsBeforeNow", defaultValue = "300") String secondsBeforeNow,
                                      @RequestBody MatcherListWrapper matcherListWrapper) {
        return messageManager.getUnProcessedMessagesForPlatform(matcherListWrapper.getMatchers(), platform, secondsBeforeNow);
    }

    @RequestMapping(value = "/uniqueMessages", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<FromUserMessage> getMessagesAndMarkThemAsProcessed(@RequestHeader(value = "X-Consumer-Username") String platform,
                                                            @RequestParam(value = "secondsBeforeNow", defaultValue = "300") String secondsBeforeNow,
                                                            @RequestBody MatcherListWrapper matcherListWrapper) {
        List<FromUserMessage> messageList = messageManager.getUnProcessedMessagesForPlatform(matcherListWrapper.getMatchers(), platform, secondsBeforeNow);
        for (FromUserMessage message : messageList) {
            messageManager.markMessageAsProcessed(message.getId(), platform);
        }
        return messageList;
    }

    @RequestMapping(value = "/messagesForUser", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<FromUserMessage> getMessagesForUserUsingClientID(@RequestHeader(value = "X-Consumer-Username") String platform,
                                                          @RequestParam(value = "secondsBeforeNow", defaultValue = "300") String secondsBeforeNow,
                                                          @RequestParam(value = "clientID") String clientID,
                                                          @RequestBody MatcherListWrapper matcherListWrapper) {
        return messageManager.getUnProcessedMessagesForPlatformAndUser(matcherListWrapper.getMatchers(), platform, secondsBeforeNow, clientID, platform);
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void addMessage(@RequestHeader(value = "X-Consumer-Username") String platform,
                           @RequestBody Message message) {
        messageManager.addMessage(platform, message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(@RequestHeader(value = "X-Consumer-Username") String platform,
                              @RequestParam(value = "id") Integer messageID) {
        messageManager.markMessageAsProcessed(messageID, platform);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(@RequestHeader(value = "X-Consumer-Username") String node,
                         @RequestBody Reply reply) {
        messageManager.addReply(node, reply);
    }

    @RequestMapping(value = "/directMessage", method = RequestMethod.PUT)
    public void addDirectMessage(@RequestHeader(value = "X-Consumer-Username") String node,
                                 @RequestBody ServerMessage serverMessage) {
        messageManager.addDirectMessage(node, serverMessage);
    }

    @RequestMapping(value = "/directMessage/{token}", method = {RequestMethod.GET, RequestMethod.PUT})
    public String addDirectMessageToUser(@RequestHeader(value = "X-Consumer-Username", defaultValue = "") String node,
                                         @PathVariable(value = "token") String token,
                                         @RequestParam(value = "message") String message) throws TokenNotFoundException {
        messageManager.addDirectMessage(node, token, message);

        return "Message sent";
    }

    @RequestMapping(value = "/directReply/{messageID}", method = RequestMethod.PUT)
    public void addDirectMessageForMessageID(@RequestHeader(value = "X-Consumer-Username") String node,
                                             @PathVariable("messageID") Integer messageID,
                                             @RequestParam("message") String message) {
        messageManager.addDirectMessageForMessageID(node, messageID, message);
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getReplies(@RequestHeader(value = "X-Consumer-Username") String platform,
                                   @RequestBody MatcherListWrapper matcherListWrapper) {
        return messageManager.getUnProcessedReplies(matcherListWrapper.getMatchers(), platform);
    }

    @RequestMapping(value = "/autoDeleteReplies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getRepliesAndMarkThemAsProcessed(@RequestHeader(value = "X-Consumer-Username") String platform,
                                                         @RequestBody MatcherListWrapper matcherListWrapper) {
        List<ToUserMessage> unProcessedReplies = messageManager.getUnProcessedReplies(matcherListWrapper.getMatchers(), platform);
        for (ToUserMessage unProcessedReply : unProcessedReplies) {
            messageManager.markReplyAsProcessed(unProcessedReply.getId(), platform);
        }
        return unProcessedReplies;
    }

    @RequestMapping(value = "/autoDeleteRepliesForClientID", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getRepliesAndMarkThemAsProcessedForClientID(@RequestHeader(value = "X-Consumer-Username") String platform,
                                                                    @RequestBody MatcherListWrapper matcherListWrapper,
                                                                    @RequestParam(value = "clientID") String clientID) {
        List<ToUserMessage> unProcessedReplies = messageManager.getUnProcessedReplies(matcherListWrapper.getMatchers(), platform)
                .stream().filter(toUserMessage -> toUserMessage.getTarget().equals(clientID)).collect(Collectors.toList());

        for (ToUserMessage unProcessedReply : unProcessedReplies) {
            messageManager.markReplyAsProcessed(unProcessedReply.getId(), platform);
        }
        return unProcessedReplies;
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(@RequestHeader(value = "X-Consumer-Username") String platform,
                            @RequestParam(value = "id") Integer replyID) {
        messageManager.markReplyAsProcessed(replyID, platform);
    }
}
