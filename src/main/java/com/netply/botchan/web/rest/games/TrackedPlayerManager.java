package com.netply.botchan.web.rest.games;

import java.util.List;

public interface TrackedPlayerManager {
    List<String> getTrackedPlayers(Integer clientID);
}
