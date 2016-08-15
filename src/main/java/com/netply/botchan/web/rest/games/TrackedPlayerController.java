package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.TrackPlayerRequest;
import com.netply.botchan.web.model.User;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TrackedPlayerController {
    private SessionHandler sessionHandler;
    private TrackedPlayerManager trackedPlayerManager;


    @Autowired
    public TrackedPlayerController(SessionHandler sessionHandler, TrackedPlayerManager trackedPlayerManager) {
        this.sessionHandler = sessionHandler;
        this.trackedPlayerManager = trackedPlayerManager;
    }

    @RequestMapping(value = "/trackedPlayers", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody List<String> getTrackedPlayers(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody User user) {
        sessionHandler.checkSessionKey(sessionKey);

        return trackedPlayerManager.getTrackedPlayers(user).stream().distinct().collect(Collectors.toList());
    }

    @RequestMapping(value = "/trackedPlayer", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void trackPlayer(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody TrackPlayerRequest trackPlayerRequest) {
        sessionHandler.checkSessionKey(sessionKey);

        if (trackPlayerRequest.getUser().getPlatform() == null) {
            throw new InvalidParameterException();
        }

        trackedPlayerManager.trackPlayer(trackPlayerRequest.getUser(), trackPlayerRequest.getPlayerName());
    }

    @RequestMapping(value = "/trackers", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody List<User> getTrackers(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "player") String trackedPlayer) {
        sessionHandler.checkSessionKey(sessionKey);

        List<User> trackers = trackedPlayerManager.getTrackers(trackedPlayer);
        if (platform != null) {
            trackers = trackers.stream().filter(user -> platform.equals(user.getPlatform())).collect(Collectors.toList());
        }
        return trackers;
    }
}
