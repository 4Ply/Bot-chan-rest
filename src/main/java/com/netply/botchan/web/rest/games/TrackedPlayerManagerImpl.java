package com.netply.botchan.web.rest.games;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class TrackedPlayerManagerImpl implements TrackedPlayerManager {
    private MultiValueMap<Integer, String> trackedPlayersMap = new LinkedMultiValueMap<>();


    @Override
    public List<String> getTrackedPlayers(Integer clientID) {
        List<String> trackedPlayers = trackedPlayersMap.get(clientID);
        if (trackedPlayers == null) {
            trackedPlayers = new ArrayList<>();
        }
        return trackedPlayers;
    }
}
