package com.fooddelivery.controller;

import com.fooddelivery.model.Edge;
import com.fooddelivery.model.Node;
import com.fooddelivery.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for accessing the city graph data.
 */
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * Returns the full city graph with nodes and edges.
     *
     * @return JSON object with "nodes" and "edges" arrays
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGraph() {
        List<Node> nodes = graphService.getAllNodes();
        List<Edge> edges = graphService.getAllEdges();

        Map<String, Object> graphData = new HashMap<>();
        graphData.put("nodes", nodes);
        graphData.put("edges", edges);

        return ResponseEntity.ok(graphData);
    }
}
