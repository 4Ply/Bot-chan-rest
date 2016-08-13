package com.netply.botchan.web.rest.nodes;

import com.netply.botchan.web.model.NodeDetails;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class NodeController {
    private SessionHandler sessionHandler;
    private NodeMessageMatcherProvider nodeMessageMatcherProvider;


    @Autowired
    public NodeController(SessionHandler sessionHandler, NodeMessageMatcherProvider nodeMessageMatcherProvider) {
        this.sessionHandler = sessionHandler;
        this.nodeMessageMatcherProvider = nodeMessageMatcherProvider;
    }

    @RequestMapping(value = "/node", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void node(
            @RequestParam(value = "sessionKey", required = false) String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestBody NodeDetails nodeDetails) {
        sessionHandler.checkSessionKey(sessionKey);

        Logger.getGlobal().log(Level.FINE, nodeDetails.toString());
        for (String messageMatcher : nodeDetails.getMessageMatchers()) {
            nodeMessageMatcherProvider.registerNode(messageMatcher, nodeDetails);
        }
    }
}
