package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackedPlayerManagerImpl implements TrackedPlayerManager {
    private TrackedPlayerDatabase trackedPlayerDatabase;


    @Autowired
    public TrackedPlayerManagerImpl(TrackedPlayerDatabase trackedPlayerDatabase) {
        this.trackedPlayerDatabase = trackedPlayerDatabase;
    }

    @Override
    public List<String> getTrackedPlayers(User user) {
        List<String> trackedPlayers = trackedPlayerDatabase.getTrackedPlayers(user);
        if (trackedPlayers == null) {
            trackedPlayers = new ArrayList<>();
        }
        return trackedPlayers;
    }

    @Override
    public void trackPlayer(User user, String playerName) {
        trackedPlayerDatabase.addTrackedPlayer(user, playerName);
    }

    @Override
    public List<User> getTrackers(String playerName) {
        return trackedPlayerDatabase.getTrackersForPlayerName(playerName)
                .stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getAllTrackedPlayers(String platform) {
        return trackedPlayerDatabase.getAllTrackedPlayersForPlatform(platform)
                .stream().distinct().collect(Collectors.toList());
    }
}
