package com.netply.botchan.web.rest.permissions;

import java.util.List;

public interface PermissionDatabase {
    boolean hasPermission(int clientID, String permission);

    void addPermission(int clientID, String permission);

    void removePermission(int clientID, String permission);

    List<Integer> getUsersThatHavePermission(String permission);
}
