package com.netply.botchan.web.rest.nodes;

import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class NodeMessageMatcherProviderImpl implements NodeMessageMatcherProvider {
    private LinkedMultiValueMap<String, Integer> multiValueMap = new LinkedMultiValueMap<>();


    @Override
    public List<Integer> getMatchingNodeIDs(String message) {
        return multiValueMap.keySet().stream()
                .filter(message::matches)
                .flatMap(s -> multiValueMap.get(s).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void registerMatcher(String regex, Integer nodeID) {
        multiValueMap.add(regex, nodeID);
    }
}
