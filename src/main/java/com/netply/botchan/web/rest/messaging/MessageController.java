package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private SessionHandler sessionHandler;
    private MessageManager messageManager;


    @Autowired
    public MessageController(SessionHandler sessionHandler, MessageManager messageManager) {
        this.sessionHandler = sessionHandler;
        this.messageManager = messageManager;
    }

    @RequestMapping(value = "/messages", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<Message> getMessages(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);

        return messageManager.getUnProcessedMessagesForPlatform(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void addMessage(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Message message) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.addMessage(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "platform") String platform,
            @RequestParam(value = "id") Integer messageID) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.markMessageAsProcessed(messageID, platform);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Reply reply) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.addReply(reply);
    }

    @RequestMapping(value = "/directMessage", method = RequestMethod.PUT)
    public void addDirectMessage(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody ServerMessage serverMessage) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.addDirectMessage(serverMessage);
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getReplies(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);

        return messageManager.getUnProcessedReplies(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "platform") String platform,
            @RequestParam(value = "id") Integer replyID) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.markReplyAsProcessed(replyID, platform);
    }
}
