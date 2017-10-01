package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

public interface UserManager {
    int getUserID(String clientID, String platform);

    User getDefaultUser(int userID);

    String getFriendlyName(String clientID, String platform);

    void setFriendlyName(String clientID, String platform, String name);

    String createPlatformOTP(String clientID, String platform);

    String createUserOTP(String clientID, String platform, String platformOTP);

    boolean linkPlatform(String clientID, String platform, String userOTP);
}
