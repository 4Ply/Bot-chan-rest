package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
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
    public @ResponseBody List<Message> messages(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestParam(value = "id") Integer id) {
        sessionHandler.checkSessionKey(sessionKey);
        sessionHandler.checkClientIDAuthorisation(sessionKey, id);

        return messageManager.getMessages(id);
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void message(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestBody Message message) {
        sessionHandler.checkSessionKey(sessionKey);

        for (Integer integer : nodeMessageMatcherProvider.getMatchingNodeIDs(message.getMessage())) {
            messageManager.addMessage(integer, message);
        }
    }
}
