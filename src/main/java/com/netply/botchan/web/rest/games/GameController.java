package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.botchan.web.model.SimpleList;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GameController {
    private SessionHandler sessionHandler;


    @Autowired
    public GameController(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @RequestMapping(value = "/trackedPlayers", consumes = "application/json", produces = "application/json")
    public @ResponseBody ArrayList<String> trackedPlayers(
            @RequestParam(value = "sessionKey", required = false) String sessionKey,
            @RequestParam(value = "botType") String botType) {
        sessionHandler.checkSessionKey(sessionKey);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("Player1");
        return strings;
    }

    @RequestMapping(value = "/currentGames", consumes = "application/json", produces = {"application/json", "text/plain"}, method = RequestMethod.POST)
    public @ResponseBody BasicResultResponse currentGames(
            @RequestParam(value = "sessionKey", required = false) String sessionKey,
            @RequestParam(value = "botType") String botType,
            @RequestBody SimpleList players) {
        sessionHandler.checkSessionKey(sessionKey);

        System.out.println("PLAYERS: " + players);

        return new BasicResultResponse(sessionHandler.isSessionValid(sessionKey), sessionKey);
    }
}
