package com.netply.botchan.web.rest.nodes;

import com.netply.botchan.web.model.NodeDetails;

import java.util.List;

public interface NodeMessageMatcherProvider {
    List<NodeDetails> getMatchingNodes(String message);

    void registerNode(String regex, NodeDetails nodeDetails);
}
