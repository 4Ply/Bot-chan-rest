package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

public interface UserDatabase {
    int getUserID(String clientID, String platform);

    int createUser();

    boolean setUserID(int userID, String clientID, String platform);

    User getUser(int userID);

    String getName(int userID);

    void setName(int userID, String name);

    String createPlatformOTP(String clientID, String platform);

    String createUserOTP(int userID, String platformOTP);

    String getPlatformOTP(String clientID, String platform);

    int getUserIDForOTP(String platformOTP, String userOTP);

    void invalidatePlatformOTP(String platformOTP);

    void invalidateAuthorisedOTP(String userOTP);
}
