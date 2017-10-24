package com.netply.botchan.web.rest.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PermissionController {
    private PermissionManager permissionManager;


    @Autowired
    public PermissionController(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @RequestMapping(value = "/hasPermission/{permission:.+}", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    Boolean hasPermission(@PathVariable(value = "permission") String permission,
                          @RequestParam("platformID") Integer platformID) {
        return permissionManager.hasPermission(platformID, permission);
    }

    @RequestMapping(value = "/permission/{permission:.+}", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void addPermission(@PathVariable(value = "permission") String permission,
                              @RequestParam("platformID") Integer platformID) {
        permissionManager.addPermission(platformID, permission);
    }

    @RequestMapping(value = "/permission/{permission:.+}", consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public void removePermission(@PathVariable(value = "permission") String permission,
                                 @RequestParam("platformID") Integer platformID) {
        permissionManager.removePermission(platformID, permission);
    }

    @RequestMapping(value = "/usersForPermission/{permission:.+}", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    List<Integer> usersForPermission(@PathVariable(value = "permission") String permission) {
        return permissionManager.getUsersThatHavePermission(permission);
    }
}
