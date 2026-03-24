package com.fooddelivery.repository;

import com.fooddelivery.model.Graph;
import com.fooddelivery.model.Node;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository holding the singleton city graph and node metadata.
 */
@Repository
public class GraphRepository {

    private final Graph graph = new Graph();
    private final Map<Integer, Node> nodeMetadata = new HashMap<>();

    /**
     * Returns the city graph instance.
     *
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Adds node metadata (name, coordinates) for a given node ID.
     *
     * @param node the node metadata
     */
    public void addNodeMetadata(Node node) {
        nodeMetadata.put(node.getId(), node);
    }

    /**
     * Returns node metadata by ID.
     *
     * @param id node ID
     * @return the node, or null if not found
     */
    public Node getNodeMetadata(int id) {
        return nodeMetadata.get(id);
    }

    /**
     * Returns all node metadata.
     *
     * @return map of node ID to Node
     */
    public Map<Integer, Node> getAllNodeMetadata() {
        return nodeMetadata;
    }
}
