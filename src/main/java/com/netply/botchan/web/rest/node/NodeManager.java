package com.netply.botchan.web.rest.node;

import com.netply.botchan.web.model.Node;
import com.netply.botchan.web.rest.error.NodeNotFoundException;

import java.util.List;

public interface NodeManager {
    void ensureNodeExists(String node);

    boolean isNodeAllowed(int platformID, String node);

    boolean isNodeAllowed(String clientID, String platform, String node);

    List<Node> listNodes(String clientID, String platform);

    void updateNodeStatus(String clientID, String platform, String enabled, boolean isEnabled) throws NodeNotFoundException;
}
