package com.netply.botchan.web.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = {
        "com.netply.botchan.web.rest",
        "com.netply.web.security.login.controller",
        "com.netply.botchan.web.rest.games",
        "com.netply.botchan.web.rest.permissions"
})
public class Application {
    public static void main(String[] args) {
        Logger.getGlobal().setLevel(Level.ALL);
        SpringApplication.run(Application.class, args);
    }
}
