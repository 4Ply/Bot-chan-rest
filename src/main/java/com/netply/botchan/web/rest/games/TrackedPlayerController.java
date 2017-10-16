package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.TrackPlayerRequest;
import com.netply.botchan.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TrackedPlayerController {
    private TrackedPlayerManager trackedPlayerManager;


    @Autowired
    public TrackedPlayerController(TrackedPlayerManager trackedPlayerManager) {
        this.trackedPlayerManager = trackedPlayerManager;
    }

    @RequestMapping(value = "/trackedPlayers", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<String> getTrackedPlayers(@RequestBody User user) {
        return trackedPlayerManager.getTrackedPlayers(user).stream().distinct().collect(Collectors.toList());
    }

    @RequestMapping(value = "/trackedPlayer", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void trackPlayer(@RequestBody TrackPlayerRequest trackPlayerRequest) {
        if (trackPlayerRequest.getUser().getPlatform() == null) {
            throw new InvalidParameterException();
        }

        trackedPlayerManager.trackPlayer(trackPlayerRequest.getUser(), trackPlayerRequest.getPlayerName());
    }

    @RequestMapping(value = "/trackedPlayer", consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public void unTrackPlayer(@RequestBody TrackPlayerRequest trackPlayerRequest) {
        if (trackPlayerRequest.getUser().getPlatform() == null) {
            throw new InvalidParameterException();
        }

        trackedPlayerManager.unTrackPlayer(trackPlayerRequest.getUser(), trackPlayerRequest.getPlayerName());
    }

    @RequestMapping(value = "/trackers", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    List<User> getTrackers(@RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "player") String trackedPlayer) {
        List<User> trackers = trackedPlayerManager.getTrackers(trackedPlayer);
        if (platform != null) {
            trackers = trackers.stream().filter(user -> platform.equals(user.getPlatform())).collect(Collectors.toList());
        }
        return trackers;
    }

    @RequestMapping(value = "/allTrackedPlayers", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getTrackers(@RequestHeader(value = "X-Consumer-Username") String platform) {
        return trackedPlayerManager.getAllTrackedPlayers(platform);
    }
}
