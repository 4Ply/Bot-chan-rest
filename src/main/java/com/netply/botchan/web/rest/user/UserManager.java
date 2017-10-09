package com.netply.botchan.web.rest.user;

import java.util.List;

public interface UserManager {
    int getUserID(String clientID, String platform);

    int getUserID(int platformID);

    int getPlatformID(String clientID, String platform);

    String getFriendlyName(String clientID, String platform);

    void setFriendlyName(String clientID, String platform, String name);

    String createPlatformOTP(String clientID, String platform);

    String createUserOTP(String clientID, String platform, String platformOTP);

    boolean linkPlatform(String clientID, String platform, String userOTP);

    List<Integer> getDefaultPlatformIDs(int userID);
}
