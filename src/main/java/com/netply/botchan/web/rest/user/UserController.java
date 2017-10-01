package com.netply.botchan.web.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserManager userManager;


    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
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
}
