package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManagerImpl implements UserManager {
    private UserDatabase userDatabase;


    @Autowired
    public UserManagerImpl(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public int getUserID(String clientID, String platform) {
        int userID = userDatabase.getUserID(clientID, platform);
        if (userID == -1) {
            int newUserID = userDatabase.createUser();
            if (userDatabase.setUserID(newUserID, clientID, platform)) {
                return newUserID;
            }

            throw new RuntimeException("Unable to retrieve userID");
        }
        return userID;
    }

    @Override
    public User getDefaultUser(int userID) {
        return userDatabase.getUser(userID);
    }
}
