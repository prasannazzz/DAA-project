package com.fooddelivery.controller;

import com.fooddelivery.dto.RouteResponse;
import com.fooddelivery.service.DijkstraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for route computation using Dijkstra's algorithm.
 */
@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final DijkstraService dijkstraService;

    public RouteController(DijkstraService dijkstraService) {
        this.dijkstraService = dijkstraService;
    }

    /**
     * Computes the shortest path between two nodes.
     *
     * @param from source node ID
     * @param to   destination node ID
     * @return RouteResponse with path, distance, and node names
     */
    @GetMapping
    public ResponseEntity<RouteResponse> getShortestRoute(
            @RequestParam int from, @RequestParam int to) {
        RouteResponse route = dijkstraService.findShortestRoute(from, to);
        return ResponseEntity.ok(route);
    }
}
