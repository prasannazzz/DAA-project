package com.fooddelivery.algorithm;

import com.fooddelivery.model.Edge;
import com.fooddelivery.model.Graph;

import java.util.*;

/**
 * Implements Dijkstra's single-source shortest path algorithm.
 * Uses a PriorityQueue (min-heap) for efficient minimum distance extraction.
 *
 * <p><b>Time Complexity:</b> O((V + E) log V) where V = vertices, E = edges.
 * The PriorityQueue offers O(log V) for insert/extract-min operations.</p>
 *
 * <p><b>Space Complexity:</b> O(V) for the distance array and predecessor map.</p>
 */
public class DijkstraAlgorithm {

    /**
     * Result holder for shortest path computation.
     */
    public static class DijkstraResult {
        private final List<Integer> path;
        private final int totalDistance;

        public DijkstraResult(List<Integer> path, int totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }

        public List<Integer> getPath() { return path; }
        public int getTotalDistance() { return totalDistance; }
    }

    /**
     * Computes the shortest path from source to destination using Dijkstra's algorithm.
     *
     * <p>Algorithm steps:
     * 1. Initialize distances to infinity, source distance to 0
     * 2. Use a min-heap PriorityQueue with (distance, nodeId) pairs
     * 3. For each extracted node, relax all outgoing edges
     * 4. Track predecessors to reconstruct the shortest path</p>
     *
     * @param graph       the city graph with weighted directed edges
     * @param source      the starting node ID
     * @param destination the target node ID
     * @return DijkstraResult containing the shortest path and total distance,
     *         or null if no path exists
     * @throws IllegalArgumentException if source or destination is not in the graph
     */
    public DijkstraResult findShortestPath(Graph graph, int source, int destination) {
        Set<Integer> allNodes = graph.getAllNodeIds();

        if (!allNodes.contains(source) || !allNodes.contains(destination)) {
            throw new IllegalArgumentException(
                    "Source (" + source + ") or destination (" + destination + ") not found in graph");
        }

        // Distance map: nodeId -> shortest known distance from source
        Map<Integer, Integer> dist = new HashMap<>();
        // Predecessor map: nodeId -> previous node in shortest path
        Map<Integer, Integer> prev = new HashMap<>();
        // Visited set to avoid re-processing nodes
        Set<Integer> visited = new HashSet<>();

        // Initialize all distances to infinity
        for (int nodeId : allNodes) {
            dist.put(nodeId, Integer.MAX_VALUE);
        }
        dist.put(source, 0);

        // Min-heap: int[]{distance, nodeId} — sorted by distance
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentDist = current[0];
            int currentNode = current[1];

            // Skip if already processed (stale entry in PQ)
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);

            // Early termination: found shortest path to destination
            if (currentNode == destination) {
                break;
            }

            // Relax all outgoing edges from currentNode
            for (Edge edge : graph.getNeighbors(currentNode)) {
                int neighbor = edge.getTo();
                if (visited.contains(neighbor)) {
                    continue;
                }

                int newDist = currentDist + edge.getWeight();
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, currentNode);
                    pq.offer(new int[]{newDist, neighbor});
                }
            }
        }

        // Check if destination is reachable
        if (dist.get(destination) == Integer.MAX_VALUE) {
            return null; // No path exists
        }

        // Reconstruct path by backtracking through predecessors
        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (prev.containsKey(current)) {
            path.add(current);
            current = prev.get(current);
        }
        path.add(source);
        Collections.reverse(path);

        return new DijkstraResult(path, dist.get(destination));
    }
}
