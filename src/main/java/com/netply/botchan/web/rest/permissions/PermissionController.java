package com.netply.botchan.web.rest.permissions;

import com.netply.botchan.web.model.User;
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

    @RequestMapping(value = "/hasPermission", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    Boolean hasPermission(@RequestBody User user,
            @RequestParam(value = "permission") String permission) {

        return permissionManager.hasPermission(user, permission);
    }

    @RequestMapping(value = "/permission", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public void addPermission(@RequestBody User user,
            @RequestParam(value = "permission") String permission) {

        permissionManager.addPermission(user, permission);
    }

    @RequestMapping(value = "/permission", consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public void removePermission(@RequestBody User user,
            @RequestParam(value = "permission") String permission) {

        permissionManager.removePermission(user, permission);
    }

    @RequestMapping(value = "/usersForPermission", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody
    List<Integer> usersForPermission(@RequestParam(value = "permission") String permission) {

        return permissionManager.getUsersThatHavePermission(permission);
    }
}
