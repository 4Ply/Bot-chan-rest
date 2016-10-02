package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;

import java.util.List;

public interface TrackedPlayerManager {
    List<String> getTrackedPlayers(User user);

    void trackPlayer(User user, String playerName);

    List<User> getTrackers(String playerName);

    List<String> getAllTrackedPlayers(String platform);
}
