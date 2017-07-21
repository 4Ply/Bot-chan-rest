package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

public interface UserDatabase {
    int getUserID(String clientID, String platform);

    int createUser();

    void setUserID(int userID, String clientID, String platform);

    User getUser(int userID);
}
