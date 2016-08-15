package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.MatcherList;
import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
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
    public @ResponseBody List<Message> getMessages(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);
        Integer clientID = matcherList.getId();
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        return messageManager.getMessagesExcludingOnesDeletedForID(matcherList.getMatchers(), clientID);
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
            @RequestParam(value = "clientID") Integer clientID,
            @RequestParam(value = "id") String messageID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        messageManager.markMessageAsProcessed(messageID, clientID);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Reply reply) {
        sessionHandler.checkSessionKey(sessionKey);

        messageManager.addReply(reply);
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody List<Reply> getReplies(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);
        Integer clientID = matcherList.getId();
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        return messageManager.getRepliesExcludingOnesDeletedForID(matcherList.getMatchers(), clientID);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "clientID") Integer clientID,
            @RequestParam(value = "id") String replyID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        messageManager.markReplyAsProcessed(replyID, clientID);
    }
}
