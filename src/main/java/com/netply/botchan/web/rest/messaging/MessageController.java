package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MessageController {
    private SessionHandler sessionHandler;


    @Autowired
    public MessageController(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @RequestMapping(value = "/messages", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody ArrayList<Message> messages(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestParam(value = "entity") String entityName) {
        sessionHandler.checkSessionKey(sessionKey);

        ArrayList<Message> messages = new ArrayList<>();
        return messages;
    }
}
