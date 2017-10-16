package com.netply.botchan.web.rest.events;

import com.netply.botchan.web.model.Event;

import java.util.List;

public interface EventManager {
    void addEvent(Event event);

    void markEventAsProcessed(String eventID, String platform);

    List<Event> getEventsExcludingOnesDeletedForID(List<String> eventTypeMatchers, String platform);
}
