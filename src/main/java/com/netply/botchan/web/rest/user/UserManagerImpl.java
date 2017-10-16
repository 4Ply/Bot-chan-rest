package com.netply.botchan.web.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public int getUserID(int platformID) {
        return userDatabase.getUserID(platformID);
    }

    @Override
    public int getPlatformID(String clientID, String platform) {
        int platformID = userDatabase.getPlatformID(clientID, platform);
        if (platformID == -1) {
            int newUserID = userDatabase.createUser();
            if (userDatabase.setUserID(newUserID, clientID, platform)) {
                return userDatabase.getPlatformID(clientID, platform);
            }

            throw new RuntimeException("Unable to retrieve platformID");
        }
        return platformID;
    }

    @Override
    public List<Integer> getDefaultPlatformIDs(int userID) {
        return userDatabase.getDefaultPlatformIDs(userID);
    }

    @Override
    public String getFriendlyName(String clientID, String platform) {
        int userID = getUserID(clientID, platform);
        return userDatabase.getName(userID);
    }

    @Override
    public void setFriendlyName(String clientID, String platform, String name) {
        int userID = getUserID(clientID, platform);
        userDatabase.setName(userID, name);
    }

    @Override
    public String createPlatformOTP(int platformID, String platform) {
        return userDatabase.createPlatformOTP(platformID);
    }

    @Override
    public String createUserOTP(int platformID, String platform, String platformOTP) {
        return userDatabase.createUserOTP(getUserID(platformID), platformOTP);
    }

    @Override
    public boolean linkPlatform(int platformID, String platform, String userOTP) {
        String platformOTP = userDatabase.getPlatformOTP(platformID);
        int userID = userDatabase.getUserIDForOTP(platformOTP, userOTP);
        boolean result = userDatabase.setUserID(userID, platformID);
        userDatabase.invalidatePlatformOTP(platformOTP);
        userDatabase.invalidateAuthorisedOTP(userOTP);
        return result;
    }
}
