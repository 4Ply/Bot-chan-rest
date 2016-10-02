package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.model.User;

import java.util.List;

public interface TrackedPlayerDatabase {
    List<String> getTrackedPlayers(User user);

    void addTrackedPlayer(User user, String playerName);

    List<User> getTrackersForPlayerName(String playerName);

    List<String> getAllTrackedPlayersForPlatform(String platform);
}
