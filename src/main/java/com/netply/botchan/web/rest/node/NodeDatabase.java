package com.netply.botchan.web.rest.node;

import java.util.List;

public interface NodeDatabase {
    void ensureNodeExists(String node);

    boolean nodeExists(String node);

    boolean isNodeAllowed(int userID, String node);

    List<String> listNodes();

    void updateNodeStatus(int userID, String node, Boolean isEnabled);
}
