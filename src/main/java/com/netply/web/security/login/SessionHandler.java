package com.netply.web.security.login;

import com.netply.botchan.web.rest.error.UnauthorisedException;

public class SessionHandler {
    private final LoginDatabase database;


    public SessionHandler(LoginDatabase database) {
        this.database = database;
    }

    public boolean isSessionValid(String sessionKey) {
        return database.checkSessionKey(sessionKey);
    }

    public void checkSessionKey(String sessionKey) {
        if (!isSessionValid(sessionKey)) {
            throw new UnauthorisedException();
        }
    }
}
