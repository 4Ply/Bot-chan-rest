package com.netply.botchan.web.rest.user;

import java.util.List;

public interface UserDatabase {
    int getUserID(String clientID, String platform);

    int getUserID(int platformID);

    int getPlatformID(String clientID, String platform);

    List<Integer> getDefaultPlatformIDs(int userID);

    int createUser();

    boolean setUserID(int userID, String clientID, String platform);

    String getName(int userID);

    void setName(int userID, String name);

    String createPlatformOTP(String clientID, String platform);

    String createUserOTP(int userID, String platformOTP);

    String getPlatformOTP(String clientID, String platform);

    int getUserIDForOTP(String platformOTP, String userOTP);

    void invalidatePlatformOTP(String platformOTP);

    void invalidateAuthorisedOTP(String userOTP);
}
