package com.netply.botchan.web.rest;

import com.netply.botchan.web.model.BasicMessageObject;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private SessionHandler sessionHandler;


    @Autowired
    public GreetingController(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @RequestMapping(value = "/greeting", produces = "application/json")
    public @ResponseBody
    BasicMessageObject greeting(@RequestParam(value = "sessionKey", required = false) String sessionKey, @RequestParam(value = "name", defaultValue = "World") String name) {
        sessionHandler.checkSessionKey(sessionKey);
        return new BasicMessageObject(counter.incrementAndGet(), String.format(template, name));
    }
}
