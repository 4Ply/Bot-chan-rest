package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

import java.util.List;

public interface UserDatabase {
    int getUserID(String clientID, String platform);

    int getUserID(int platformID);

    int getPlatformID(String clientID, String platform);

    List<Integer> getDefaultPlatformIDs(int userID);

    List<User> getPlatformUsers(int userID);

    int createUser();

    boolean setUserID(int userID, String clientID, String platform);

    boolean setUserID(int userID, int platformID);

    String getName(int userID);

    void setName(int userID, String name);

    String createPlatformOTP(int platformID);

    String createUserOTP(int userID, String platformOTP);

    String getPlatformOTP(int platformID);

    int getUserIDForOTP(String platformOTP, String userOTP);

    void invalidatePlatformOTP(String platformOTP);

    void invalidateAuthorisedOTP(String userOTP);
}
