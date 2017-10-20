package com.netply.botchan.web.rest.node;

import com.netply.botchan.web.model.Node;
import com.netply.botchan.web.rest.error.NodeNotFoundException;
import com.netply.botchan.web.rest.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NodeManagerImpl implements NodeManager {
    private NodeDatabase nodeDatabase;
    private UserManager userManager;


    @Autowired
    public NodeManagerImpl(NodeDatabase nodeDatabase, UserManager userManager) {
        this.nodeDatabase = nodeDatabase;
        this.userManager = userManager;
    }

    @Override
    public void ensureNodeExists(String node) {
        nodeDatabase.ensureNodeExists(node);
    }

    @Override
    public boolean isNodeAllowedForUserID(int userID, String node) {
        return nodeDatabase.isNodeAllowed(userID, node);
    }

    @Override
    public boolean isNodeAllowedForPlatformID(int platformID, String node) {
        return isNodeAllowedForUserID(userManager.getUserID(platformID), node);
    }

    @Override
    public boolean isNodeAllowed(String clientID, String platform, String node) {
        return nodeDatabase.isNodeAllowed(userManager.getUserID(clientID, platform), node);
    }

    @Override
    public List<Node> listNodes(String clientID, String platform) {
        int userID = userManager.getUserID(clientID, platform);

        List<String> nodesNames = nodeDatabase.listNodes();
        return nodesNames.stream()
                .map(s -> new Node(s, nodeDatabase.isNodeAllowed(userID, s)))
                .collect(Collectors.toList());
    }

    @Override
    public void updateNodeStatus(String clientID, String platform, String node, boolean isEnabled) throws NodeNotFoundException {
        int userID = userManager.getUserID(clientID, platform);
        if (nodeDatabase.nodeExists(node)) {
            nodeDatabase.updateNodeStatus(userID, node, isEnabled);
        } else {
            throw new NodeNotFoundException();
        }
    }
}
