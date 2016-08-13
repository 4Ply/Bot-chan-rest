package com.netply.botchan.web.rest.nodes;

import com.netply.botchan.web.model.NodeDetails;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

public class NodeMessageMatcherProviderImpl implements NodeMessageMatcherProvider {
    private LinkedMultiValueMap<String, NodeDetails> multiValueMap = new LinkedMultiValueMap<>();


    @Override
    public List<NodeDetails> getMatchingNodes(String message) {
        return multiValueMap.keySet().stream()
                .filter(message::matches)
                .flatMap(s -> multiValueMap.get(s).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void registerNode(String regex, NodeDetails nodeDetails) {
        multiValueMap.add(regex, nodeDetails);
    }
}
