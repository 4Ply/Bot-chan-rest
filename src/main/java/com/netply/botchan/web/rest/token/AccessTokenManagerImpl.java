package com.netply.botchan.web.rest.token;

import com.netply.botchan.web.rest.error.TokenNotFoundException;
import com.netply.botchan.web.rest.node.NodeManager;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class AccessTokenManagerImpl implements AccessTokenManager {
    private AccessTokenDatabase accessTokenDatabase;
    private UserManager userManager;
    private NodeManager nodeManager;


    @Autowired
    public AccessTokenManagerImpl(AccessTokenDatabase accessTokenDatabase, UserManager userManager, NodeManager nodeManager) {
        this.accessTokenDatabase = accessTokenDatabase;
        this.userManager = userManager;
        this.nodeManager = nodeManager;
    }

    @Override
    public int getUserID(String token) throws TokenNotFoundException {
        Optional<Integer> userID = accessTokenDatabase.getUserID(token);
        if (!userID.isPresent()) {
            throw new TokenNotFoundException();
        }
        return userID.get();
    }

    @Override
    public String generateToken(String clientID, String platform) {
        int userID = userManager.getUserID(clientID, platform);

        return accessTokenDatabase.generateToken(userID);
    }

    @Override
    public ArrayList<String> listTokens(String clientID, String platform) {
        int userID = userManager.getUserID(clientID, platform);

        return new ArrayList<>(accessTokenDatabase.listTokens(userID));
    }
}
