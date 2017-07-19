package com.netply.botchan.web.rest.user;

public interface UserDatabase {
    int getUserID(String clientID, String platform);

    int createUser();

    void setUserID(int userID, String clientID, String platform);
}
