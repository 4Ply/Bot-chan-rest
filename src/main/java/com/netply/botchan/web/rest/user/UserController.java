package com.netply.botchan.web.rest.user;

import com.netply.botchan.web.rest.error.UnauthorisedException;
import com.netply.botchan.web.rest.node.NodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String createPlatformOTP(@RequestParam(value = "sender") String sender,
                                    @RequestParam(value = "platform") String platform) {
        return userManager.createPlatformOTP(sender, platform);
    }

    @RequestMapping(value = "/userOTP", method = RequestMethod.GET)
    public String createUserOTP(@RequestParam(value = "sender") String sender,
                                @RequestParam(value = "platform") String platform,
                                @RequestParam(value = "platformOTP") String platformOTP) {
        return userManager.createUserOTP(sender, platform, platformOTP);
    }

    @RequestMapping(value = "/linkPlatform", method = RequestMethod.GET)
    public boolean linkPlatform(@RequestParam(value = "sender") String sender,
                                @RequestParam(value = "platform") String platform,
                                @RequestParam(value = "userOTP") String userOTP) {
        return userManager.linkPlatform(sender, platform, userOTP);
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getFriendlyName(@RequestParam(value = "sender") String sender,
                                  @RequestParam(value = "platform") String platform) {
        return userManager.getFriendlyName(sender, platform);
    }

    @RequestMapping(value = "/name", method = RequestMethod.PATCH)
    public void updateFriendlyName(@RequestParam(value = "sender") String sender,
                                   @RequestParam(value = "platform") String platform,
                                   @RequestParam(value = "name") String name) {
        userManager.setFriendlyName(sender, platform, name);
    }

    @RequestMapping(value = "/userID", method = RequestMethod.GET)
    public Integer getUserID(@RequestParam(value = "platformID") Integer platformID,
                                  @RequestParam(value = "platform") String platform) {
        int userID = userManager.getUserID(platformID);
        if (!nodeManager.isNodeAllowed(userID, platform)) {
            throw new UnauthorisedException("Node not authorised for this user");
        }
        return userID;
    }
}
