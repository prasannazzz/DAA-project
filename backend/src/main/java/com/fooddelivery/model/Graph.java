package com.fooddelivery.model;

import java.util.*;

/**
 * Represents the city road network as a weighted directed graph.
 * Uses an adjacency list representation for efficient traversal.
 */
public class Graph {

    private final Map<Integer, List<Edge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a directed edge to the graph.
     * Creates the source node's adjacency list if it doesn't exist.
     *
     * @param edge the directed edge to add
     */
    public void addEdge(Edge edge) {
        adjacencyList.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge);
        // Ensure the destination node exists in the adjacency list
        adjacencyList.computeIfAbsent(edge.getTo(), k -> new ArrayList<>());
    }

    /**
     * Returns all outgoing edges from a given node.
     *
     * @param nodeId the source node ID
     * @return list of outgoing edges, or empty list if node has no edges
     */
    public List<Edge> getNeighbors(int nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    /**
     * Returns the set of all node IDs present in the graph.
     *
     * @return set of node IDs
     */
    public Set<Integer> getAllNodeIds() {
        return adjacencyList.keySet();
    }

    /**
     * Returns all edges in the graph.
     *
     * @return list of all edges
     */
    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjacencyList.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    /**
     * Returns the full adjacency list representation.
     *
     * @return adjacency list map
     */
    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return node count
     */
    public int getNodeCount() {
        return adjacencyList.size();
    }
}
