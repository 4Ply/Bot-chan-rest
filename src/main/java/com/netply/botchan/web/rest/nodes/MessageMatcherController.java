package com.netply.botchan.web.rest.nodes;

import com.netply.botchan.web.model.MatcherList;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MessageMatcherController {
    private SessionHandler sessionHandler;
    private NodeMessageMatcherProvider nodeMessageMatcherProvider;


    @Autowired
    public MessageMatcherController(SessionHandler sessionHandler, NodeMessageMatcherProvider nodeMessageMatcherProvider) {
        this.sessionHandler = sessionHandler;
        this.nodeMessageMatcherProvider = nodeMessageMatcherProvider;
    }

    @RequestMapping(value = "/messageMatchers", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void node(
            @RequestParam(value = "sessionKey", required = false) String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);

        Logger.getGlobal().log(Level.FINE, matcherList.toString());
        for (String messageMatcher : matcherList.getMessageMatchers()) {
            nodeMessageMatcherProvider.registerMatcher(messageMatcher, matcherList.getId());
        }
    }
}
