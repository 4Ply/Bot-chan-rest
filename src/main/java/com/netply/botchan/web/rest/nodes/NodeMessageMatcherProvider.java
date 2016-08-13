package com.netply.botchan.web.rest.nodes;

import java.util.List;

public interface NodeMessageMatcherProvider {
    List<Integer> getMatchingNodeIDs(String message);

    void registerMatcher(String regex, Integer nodeID);
}
