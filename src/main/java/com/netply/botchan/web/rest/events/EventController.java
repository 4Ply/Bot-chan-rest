package com.netply.botchan.web.rest.events;

import com.netply.botchan.web.model.Event;
import com.netply.botchan.web.model.MatcherList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    private EventManager eventManager;


    @Autowired
    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @RequestMapping(value = "/events", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<Event> getEvents(@RequestBody MatcherList matcherList) {
        String platform = matcherList.getPlatform();

        return eventManager.getEventsExcludingOnesDeletedForID(matcherList.getMatchers(), platform);
    }

    @RequestMapping(value = "/event", method = RequestMethod.PUT)
    public void addEvent(@RequestBody Event event) {
        eventManager.addEvent(event);
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    public void deleteEvent(
            @RequestHeader(value = "X-Consumer-Username") String platform,
            @RequestParam(value = "id") String eventID) {

        eventManager.markEventAsProcessed(eventID, platform);
    }
}
