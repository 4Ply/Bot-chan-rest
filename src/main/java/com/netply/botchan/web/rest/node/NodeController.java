package com.netply.botchan.web.rest.node;

import com.netply.botchan.web.model.Node;
import com.netply.botchan.web.rest.error.NodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NodeController {
    private NodeManager nodeManager;


    @Autowired
    public NodeController(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public @ResponseBody
    List<Node> listNodes(@RequestParam(value = "sender") String sender,
                         @RequestHeader(value = "X-Consumer-Username") String platform) {
        return nodeManager.listNodes(sender, platform);
    }

    @RequestMapping(value = "/nodeStatus", method = RequestMethod.PATCH)
    public void updateNode(@RequestParam(value = "sender") String sender,
                           @RequestHeader(value = "X-Consumer-Username") String platform,
                           @RequestParam(value = "node") String node,
                           @RequestParam(value = "enabled") boolean isEnabled) throws NodeNotFoundException {
        nodeManager.updateNodeStatus(sender, platform, node, isEnabled);
    }
}
