package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;

public interface UserManager {
    int getUserID(String clientID, String platform);

    User getDefaultUser(int userID);
}
