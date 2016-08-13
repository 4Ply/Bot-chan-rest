package com.netply.botchan.web.rest.nodes;

import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class NodeMessageMatcherProviderImpl implements NodeMessageMatcherProvider {
    private LinkedMultiValueMap<String, Integer> matcherNodeMap = new LinkedMultiValueMap<>();
    private LinkedMultiValueMap<String, Integer> platformNodeMap = new LinkedMultiValueMap<>();


    @Override
    public List<Integer> getMatchingNodeIDs(String message) {
        return matcherNodeMap.keySet().stream()
                .filter(message::matches)
                .flatMap(s -> matcherNodeMap.get(s).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void registerMatcher(String regex, Integer nodeID) {
        matcherNodeMap.add(regex, nodeID);
    }

    @Override
    public List<Integer> getNodeIDsForPlatform(String platform) {
        return platformNodeMap.keySet().stream()
                .filter(platform::equals)
                .flatMap(s -> platformNodeMap.get(s).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void registerNodeForPlatform(String platform, Integer nodeID) {
        platformNodeMap.add(platform, nodeID);
    }
}
