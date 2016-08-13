package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
import com.netply.botchan.web.rest.nodes.NodeMessageMatcherProvider;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private SessionHandler sessionHandler;
    private NodeMessageMatcherProvider nodeMessageMatcherProvider;
    private MessageManager messageManager;


    @Autowired
    public MessageController(SessionHandler sessionHandler, NodeMessageMatcherProvider nodeMessageMatcherProvider, MessageManager messageManager) {
        this.sessionHandler = sessionHandler;
        this.nodeMessageMatcherProvider = nodeMessageMatcherProvider;
        this.messageManager = messageManager;
    }

    @RequestMapping(value = "/messages", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody List<Message> getMessages(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "id") Integer clientID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        return messageManager.getMessages(clientID);
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void addMessage(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Message message) {
        sessionHandler.checkSessionKey(sessionKey);

        for (Integer integer : nodeMessageMatcherProvider.getMatchingNodeIDs(message.getMessage())) {
            messageManager.addMessage(integer, message);
        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "clientID") Integer clientID,
            @RequestParam(value = "id") String messageID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        messageManager.deleteMessage(clientID, messageID);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Reply reply) {
        sessionHandler.checkSessionKey(sessionKey);

        for (Integer integer : nodeMessageMatcherProvider.getNodeIDsForPlatform(reply.getPlatform())) {
            messageManager.addReply(integer, reply);
        }
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody List<Reply> getReplies(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "id") Integer clientID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        return messageManager.getReplies(clientID);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "clientID") Integer clientID,
            @RequestParam(value = "id") String replyID) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, clientID);

        messageManager.deleteReply(clientID, replyID);
    }
}
