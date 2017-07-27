package com.netply.web.security.login;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.botchan.web.rest.error.InvalidCredentialsException;

@Deprecated
public class LoginHandler {
    private LoginDatabase database;


    public LoginHandler(LoginDatabase database) {
        this.database = database;
    }

    public BasicResultResponse login(String username, String passwordHash) {
        try {
            return database.login(username, passwordHash);
        } catch (InvalidCredentialsException e) {
            return new BasicResultResponse("Invalid credentials");
        }
    }
}
