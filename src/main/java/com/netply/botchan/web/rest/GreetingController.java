package com.netply.botchan.web.rest;

import com.netply.botchan.web.model.BasicMessageObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping(value = "/greeting", produces = "application/json")
    public @ResponseBody
    BasicMessageObject greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new BasicMessageObject(counter.incrementAndGet(), String.format(template, name));
    }
}
