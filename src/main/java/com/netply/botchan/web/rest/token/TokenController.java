package com.netply.botchan.web.rest.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/token")
public class TokenController {
    private AccessTokenManager accessTokenManager;


    @Autowired
    public TokenController(AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public String createToken(@RequestHeader(value = "X-Consumer-Username") String platform,
                              @RequestParam(value = "clientID") String clientID) {

        return accessTokenManager.generateToken(clientID, platform);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"application/json", "text/plain"})
    public ArrayList<String> listTokens(@RequestHeader(value = "X-Consumer-Username") String platform,
                                        @RequestParam(value = "clientID") String clientID) {

        return accessTokenManager.listTokens(clientID, platform);
    }
}
