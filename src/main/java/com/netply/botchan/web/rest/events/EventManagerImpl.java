package com.netply.botchan.web.rest.events;

import com.netply.botchan.web.model.Event;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventManagerImpl implements EventManager {
    private static EventManagerImpl instance;
    private MultiValueMap<Event, Integer> eventMap = new LinkedMultiValueMap<>();


    @Deprecated // Replace with DB
    public static EventManagerImpl getInstance() {
        if (instance == null) {
            instance = new EventManagerImpl();
        }
        return instance;
    }

    @Override
    public void addEvent(Event event) {
        event.setId(UUID.randomUUID().toString());
        eventMap.add(event, -1);
    }

    @Override
    public void markEventAsProcessed(String eventID, Integer clientID) {
        eventMap.keySet().stream()
                .filter(message -> eventID.equals(message.getId()))
                .distinct()
                .forEach(message -> eventMap.add(message, clientID));
    }

    @Override
    public List<Event> getEventsExcludingOnesDeletedForID(ArrayList<String> eventTypeMatchers, Integer integer) {
        return eventMap.keySet().stream()
                .distinct()
                .filter(doesMessageMatchAnyMessageMatcherPattern(eventTypeMatchers))
                .filter(message -> !eventMap.get(message).contains(integer))
                .collect(Collectors.toList());
    }

    private Predicate<Event> doesMessageMatchAnyMessageMatcherPattern(ArrayList<String> eventTypeMatchers) {
        return event -> {
            for (String eventType : eventTypeMatchers) {
                if (event.getEventType().equals(eventType)) {
                    return true;
                }
            }
            return false;
        };
    }
}
