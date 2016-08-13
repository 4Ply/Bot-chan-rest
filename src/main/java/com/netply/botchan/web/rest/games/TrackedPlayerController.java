package com.netply.botchan.web.rest.games;

import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrackedPlayerController {
    private SessionHandler sessionHandler;
    private TrackedPlayerManager trackedPlayerManager;


    @Autowired
    public TrackedPlayerController(SessionHandler sessionHandler, TrackedPlayerManager trackedPlayerManager) {
        this.sessionHandler = sessionHandler;
        this.trackedPlayerManager = trackedPlayerManager;
    }

    @RequestMapping(value = "/trackedPlayers", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody List<String> getTrackedPlayers(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "id") Integer clientID) {
        sessionHandler.checkSessionKey(sessionKey);

        return trackedPlayerManager.getTrackedPlayers(clientID);
    }
}
