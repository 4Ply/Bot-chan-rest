package com.netply.botchan.web.rest.token;

import com.netply.botchan.web.rest.error.TokenNotFoundException;

import java.util.ArrayList;

public interface AccessTokenManager {
    int getUserID(String token) throws TokenNotFoundException;

    String generateToken(String clientID, String platform);

    ArrayList<String> listTokens(String clientID, String platform);
}
