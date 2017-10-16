package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.model.User;
import com.netply.botchan.web.rest.error.UnauthorisedException;
import com.netply.botchan.web.rest.node.NodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserManager userManager;
    private NodeManager nodeManager;


    @Autowired
    public UserController(UserManager userManager, NodeManager nodeManager) {
        this.userManager = userManager;
        this.nodeManager = nodeManager;
    }

    @RequestMapping(value = "/platformOTP", method = RequestMethod.GET)
    public String createPlatformOTP(@RequestHeader(value = "X-Consumer-Username") String platform,
                                    @RequestParam(value = "platformID") Integer platformID) {
        if (nodeManager.isNodeAllowed(platformID, platform)) {
            return userManager.createPlatformOTP(platformID, platform);
        } else {
            throw new UnauthorisedException();
        }
    }

    @RequestMapping(value = "/userOTP", method = RequestMethod.GET)
    public String createUserOTP(@RequestHeader(value = "X-Consumer-Username") String platform,
                                @RequestParam(value = "platformID") Integer platformID,
                                @RequestParam(value = "platformOTP") String platformOTP) {
        if (nodeManager.isNodeAllowed(platformID, platform)) {
            return userManager.createUserOTP(platformID, platform, platformOTP);
        } else {
            throw new UnauthorisedException();
        }
    }

    @RequestMapping(value = "/linkPlatform", method = RequestMethod.GET)
    public boolean linkPlatform(@RequestHeader(value = "X-Consumer-Username") String platform,
                                @RequestParam(value = "platformID") Integer platformID,
                                @RequestParam(value = "userOTP") String userOTP) {
        if (nodeManager.isNodeAllowed(platformID, platform)) {
            return userManager.linkPlatform(platformID, platform, userOTP);
        } else {
            throw new UnauthorisedException();
        }
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getFriendlyName(@RequestHeader(value = "X-Consumer-Username") String platform,
                                  @RequestParam(value = "sender") String sender) {
        return userManager.getFriendlyName(sender, platform);
    }

    @RequestMapping(value = "/name", method = RequestMethod.PATCH)
    public void updateFriendlyName(@RequestHeader(value = "X-Consumer-Username") String platform,
                                   @RequestParam(value = "sender") String sender,
                                   @RequestParam(value = "name") String name) {
        userManager.setFriendlyName(sender, platform, name);
    }

    @RequestMapping(value = "/userID", method = RequestMethod.GET)
    public Integer getUserID(@RequestHeader(value = "X-Consumer-Username") String platform,
                             @RequestParam(value = "platformID") Integer platformID) {
        if (!nodeManager.isNodeAllowed(platformID, platform)) {
            throw new UnauthorisedException("Node not authorised for this user");
        }
        return userManager.getUserID(platformID);
    }

    @RequestMapping(value = "/platformUsers", method = RequestMethod.GET)
    public List<User> getPlatformUsers(@RequestHeader(value = "X-Consumer-Username") String platform,
                                       @RequestParam(value = "clientID") String clientID) {
        if (!nodeManager.isNodeAllowed(clientID, platform, platform)) {
            throw new UnauthorisedException("Node not authorised for this user");
        }
        int userID = userManager.getUserID(clientID, platform);
        return userManager.getPlatformUsers(userID);
    }
}
