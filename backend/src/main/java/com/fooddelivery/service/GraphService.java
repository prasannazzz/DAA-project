package com.fooddelivery.service;

import com.fooddelivery.model.Edge;
import com.fooddelivery.model.Graph;
import com.fooddelivery.model.Node;
import com.fooddelivery.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for accessing the city graph data.
 */
@Service
public class GraphService {

    private final GraphRepository graphRepository;

    public GraphService(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    /**
     * Returns the city graph.
     *
     * @return the Graph instance
     */
    public Graph getGraph() {
        return graphRepository.getGraph();
    }

    /**
     * Returns all nodes with their metadata.
     *
     * @return list of all nodes
     */
    public List<Node> getAllNodes() {
        return new ArrayList<>(graphRepository.getAllNodeMetadata().values());
    }

    /**
     * Returns all edges in the graph.
     *
     * @return list of all edges
     */
    public List<Edge> getAllEdges() {
        return graphRepository.getGraph().getAllEdges();
    }

    /**
     * Returns node metadata by ID.
     *
     * @param id node ID
     * @return node metadata, or null if not found
     */
    public Node getNode(int id) {
        return graphRepository.getNodeMetadata(id);
    }

    /**
     * Returns all node metadata as a map.
     *
     * @return map of node ID to Node
     */
    public Map<Integer, Node> getNodeMap() {
        return graphRepository.getAllNodeMetadata();
    }
}
