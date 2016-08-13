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

    public boolean isAuthorisedForClientId(String sessionKey, Integer clientID) {
        return database.isAuthorisedForClientId(sessionKey, clientID);
    }

    public void checkSessionKey(String sessionKey) {
        if (!isSessionValid(sessionKey)) {
            throw new UnauthorisedException();
        }
    }

    public void checkClientIDAuthorisation(String sessionKey, Integer clientID) {
        if (!isAuthorisedForClientId(sessionKey, clientID)) {
            throw new UnauthorisedException();
        }
    }
}
