package com.netply.botchan.web.rest.permissions;

import com.netply.botchan.web.model.User;

import java.util.List;

public interface PermissionManager {
    boolean hasPermission(User user, String permission);

    void addPermission(User user, String permission);

    void removePermission(User user, String permission);

    List<Integer> getUsersThatHavePermission(String permission);
}
