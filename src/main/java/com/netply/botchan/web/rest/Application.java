package com.netply.botchan.web.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.netply.botchan.web.rest",
        "com.netply.web.security.login.controller",
        "com.netply.botchan.web.rest.games"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
