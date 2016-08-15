package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrackedPlayerManagerImpl implements TrackedPlayerManager {
    private MultiValueMap<User, String> trackedPlayersMap = new LinkedMultiValueMap<>();


    @Override
    public List<String> getTrackedPlayers(User user) {
        List<String> trackedPlayers = trackedPlayersMap.get(user);
        if (trackedPlayers == null) {
            trackedPlayers = new ArrayList<>();
        }
        return trackedPlayers;
    }

    @Override
    public void trackPlayer(User user, String playerName) {
        trackedPlayersMap.add(user, playerName);
    }

    @Override
    public List<User> getTrackers(String playerName) {
        return trackedPlayersMap.keySet().stream()
                .filter(user -> getTrackedPlayers(user).contains(playerName))
                .distinct()
                .collect(Collectors.toList());
    }
}
