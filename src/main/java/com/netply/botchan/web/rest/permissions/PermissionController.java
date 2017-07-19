package com.netply.botchan.web.rest.permissions;

import com.netply.botchan.web.model.User;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PermissionController {
    private SessionHandler sessionHandler;
    private PermissionManager permissionManager;


    @Autowired
    public PermissionController(SessionHandler sessionHandler, PermissionManager permissionManager) {
        this.sessionHandler = sessionHandler;
        this.permissionManager = permissionManager;
    }

    @RequestMapping(value = "/hasPermission", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    Boolean hasPermission(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody User user,
            @RequestParam(value = "permission") String permission) {
        sessionHandler.checkSessionKey(sessionKey);

        return permissionManager.hasPermission(user, permission);
    }

    @RequestMapping(value = "/permission", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void addPermission(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody User user,
            @RequestParam(value = "permission") String permission) {
        sessionHandler.checkSessionKey(sessionKey);

        permissionManager.addPermission(user, permission);
    }

    @RequestMapping(value = "/permission", consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public void removePermission(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestBody User user,
            @RequestParam(value = "permission") String permission) {
        sessionHandler.checkSessionKey(sessionKey);

        permissionManager.removePermission(user, permission);
    }

    @RequestMapping(value = "/usersForPermission", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    List<Integer> usersForPermission(
            @RequestParam(value = "sessionKey") String sessionKey,
            @RequestParam(value = "permission") String permission) {
        sessionHandler.checkSessionKey(sessionKey);

        return permissionManager.getUsersThatHavePermission(permission);
    }
}
