package com.netply.botchan.web.rest.token;

import java.util.List;
import java.util.Optional;

public interface AccessTokenDatabase {
    Optional<Integer> getUserID(String token);

    String generateToken(int userID);

    List<String> listTokens(int userID);
}
