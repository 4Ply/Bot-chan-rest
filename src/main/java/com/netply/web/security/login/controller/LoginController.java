package com.netply.web.security.login.controller;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.web.security.login.LoginHandler;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private LoginHandler loginHandler;
    private SessionHandler sessionHandler;


    @Autowired
    public LoginController(LoginHandler loginHandler, SessionHandler sessionHandler) {
        this.loginHandler = loginHandler;
        this.sessionHandler = sessionHandler;
    }

    @RequestMapping(value = "/login", produces = "application/json")
    public BasicResultResponse login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String passwordHash) {
        return loginHandler.login(username, passwordHash);
    }

    @RequestMapping(value = "/loginCheck", consumes = "application/json", produces = "application/json")
    public BasicResultResponse loginCheck(@RequestParam(value = "sessionKey") String sessionKey) {
        return new BasicResultResponse(sessionHandler.isSessionValid(sessionKey), sessionKey);
    }
}
