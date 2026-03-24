package com.fooddelivery.service;

import com.fooddelivery.algorithm.DijkstraAlgorithm;
import com.fooddelivery.algorithm.DijkstraAlgorithm.DijkstraResult;
import com.fooddelivery.dto.RouteResponse;
import com.fooddelivery.exception.ApiException;
import com.fooddelivery.model.Node;
import com.fooddelivery.repository.GraphRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service wrapping Dijkstra's algorithm with graph data from the repository.
 */
@Service
public class DijkstraService {

    private final DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
    private final GraphRepository graphRepository;

    public DijkstraService(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    /**
     * Computes the shortest path between two nodes.
     *
     * @param from source node ID
     * @param to   destination node ID
     * @return RouteResponse with path, distance, and node names
     * @throws ApiException if no path exists
     */
    public RouteResponse findShortestRoute(int from, int to) {
        DijkstraResult result = dijkstra.findShortestPath(graphRepository.getGraph(), from, to);

        if (result == null) {
            throw new ApiException("No route found from node " + from + " to node " + to, HttpStatus.NOT_FOUND);
        }

        // Resolve node names for the path
        List<String> pathNames = new ArrayList<>();
        for (int nodeId : result.getPath()) {
            Node node = graphRepository.getNodeMetadata(nodeId);
            pathNames.add(node != null ? node.getName() : "Node " + nodeId);
        }

        return new RouteResponse(result.getPath(), result.getTotalDistance(), pathNames);
    }

    /**
     * Computes shortest path and returns raw DijkstraResult (used internally).
     *
     * @param from source node ID
     * @param to   destination node ID
     * @return DijkstraResult or null if no path
     */
    public DijkstraResult computePath(int from, int to) {
        return dijkstra.findShortestPath(graphRepository.getGraph(), from, to);
    }
}
