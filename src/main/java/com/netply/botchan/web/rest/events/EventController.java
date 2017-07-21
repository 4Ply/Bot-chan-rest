package com.netply.botchan.web.rest.events;

import com.netply.botchan.web.model.Event;
import com.netply.botchan.web.model.MatcherList;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    private SessionHandler sessionHandler;
    private EventManager eventManager;


    @Autowired
    public EventController(SessionHandler sessionHandler, EventManager eventManager) {
        this.sessionHandler = sessionHandler;
        this.eventManager = eventManager;
    }

    @RequestMapping(value = "/events", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody List<Event> getEvents(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody MatcherList matcherList) {
        sessionHandler.checkSessionKey(sessionKey);
        String platform = matcherList.getPlatform();

        return eventManager.getEventsExcludingOnesDeletedForID(matcherList.getMatchers(), platform);
    }

    @RequestMapping(value = "/event", method = RequestMethod.PUT)
    public void addEvent(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody Event event) {
        sessionHandler.checkSessionKey(sessionKey);

        eventManager.addEvent(event);
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    public void deleteEvent(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "platform") String platform,
            @RequestParam(value = "id") String eventID) {
        sessionHandler.checkSessionKey(sessionKey);

        eventManager.markEventAsProcessed(eventID, platform);
    }
}
