package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

import java.util.List;

public interface UserManager {
    int getUserID(String clientID, String platform);

    int getUserID(int platformID);

    int getPlatformID(String clientID, String platform);

    String getFriendlyName(String clientID, String platform);

    void setFriendlyName(String clientID, String platform, String name);

    String createPlatformOTP(int platformID, String platform);

    String createUserOTP(int platformID, String platform, String platformOTP);

    boolean linkPlatform(int platformID, String platform, String userOTP);

    List<Integer> getDefaultPlatformIDs(int userID);

    List<User> getPlatformUsers(int userID);
}
