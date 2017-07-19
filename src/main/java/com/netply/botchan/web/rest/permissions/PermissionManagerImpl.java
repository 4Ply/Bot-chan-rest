package com.netply.botchan.web.rest.permissions;

import com.netply.botchan.web.model.User;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionManagerImpl implements PermissionManager {
    private PermissionDatabase permissionDatabase;
    private UserManager userManager;


    @Autowired
    public PermissionManagerImpl(PermissionDatabase permissionDatabase, UserManager userManager) {
        this.permissionDatabase = permissionDatabase;
        this.userManager = userManager;
    }

    @Override
    public boolean hasPermission(User user, String permission) {
        int userID = userManager.getUserID(user.getClientID(), user.getPlatform());
        return userID != -1 && permissionDatabase.hasPermission(userID, permission);
    }

    @Override
    public void addPermission(User user, String permission) {
        int userID = userManager.getUserID(user.getClientID(), user.getPlatform());
        if (userID != -1) {
            permissionDatabase.addPermission(userID, permission);
        }
    }

    @Override
    public void removePermission(User user, String permission) {
        int userID = userManager.getUserID(user.getClientID(), user.getPlatform());
        if (userID != -1) {
            permissionDatabase.removePermission(userID, permission);
        }
    }

    @Override
    public List<Integer> getUsersThatHavePermission(String permission) {
        return permissionDatabase.getUsersThatHavePermission(permission).stream().distinct().collect(Collectors.toList());
    }
}
