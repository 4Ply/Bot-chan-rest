package com.netply.botchan.web.rest.events;

import com.netply.botchan.web.model.Event;

import java.util.ArrayList;
import java.util.List;

public interface EventManager {
    void addEvent(Event event);

    void markEventAsProcessed(String eventID, Integer clientID);

    List<Event> getEventsExcludingOnesDeletedForID(ArrayList<String> eventTypeMatchers, Integer integer);
}
