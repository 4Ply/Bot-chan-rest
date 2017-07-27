package com.netply.web.security.login;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.botchan.web.rest.error.InvalidCredentialsException;

@Deprecated
public interface LoginDatabase {
    BasicResultResponse login(String username, String password) throws InvalidCredentialsException;

    String generateSessionKey(int userId);

    boolean checkSessionKey(String sessionKey);

    boolean isAuthorisedForClientId(String sessionKey, Integer clientId);
}
