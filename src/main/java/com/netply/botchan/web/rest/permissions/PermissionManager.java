package com.netply.botchan.web.rest.permissions;

import java.util.List;

public interface PermissionManager {
    boolean hasPermission(int platformID, String permission);

    void addPermission(int platformID, String permission);

    void removePermission(int platformID, String permission);

    List<Integer> getUsersThatHavePermission(String permission);
}
