package com.netply.botchan.web.rest.games;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class TrackedPlayerManagerImpl implements TrackedPlayerManager {
    private MultiValueMap<String, String> trackedPlayersMap = new LinkedMultiValueMap<>();


    @Override
    public List<String> getTrackedPlayers(String clientID) {
        List<String> trackedPlayers = trackedPlayersMap.get(clientID);
        if (trackedPlayers == null) {
            trackedPlayers = new ArrayList<>();
        }
        return trackedPlayers;
    }

    @Override
    public void trackPlayer(String clientID, String playerName) {
        trackedPlayersMap.add(clientID, playerName);
    }
}
