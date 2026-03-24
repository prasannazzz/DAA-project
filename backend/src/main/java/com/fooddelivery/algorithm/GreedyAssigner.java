package com.fooddelivery.algorithm;

import com.fooddelivery.model.Agent;
import com.fooddelivery.model.Graph;

import java.util.List;

/**
 * Greedy algorithm for assigning delivery agents to orders.
 *
 * <p>Strategy: For a given restaurant node, iterate over all available agents,
 * compute the shortest path from each agent's current location to the restaurant,
 * and select the agent with the minimum travel time.</p>
 *
 * <p><b>Time Complexity:</b> O(A × (V + E) log V) where A = number of available agents.
 * We run Dijkstra once per available agent.</p>
 *
 * <p><b>Space Complexity:</b> O(V) for each Dijkstra invocation (reused).</p>
 */
public class GreedyAssigner {

    private final DijkstraAlgorithm dijkstra;

    public GreedyAssigner() {
        this.dijkstra = new DijkstraAlgorithm();
    }

    /**
     * Result holder for greedy assignment.
     */
    public static class AssignmentResult {
        private final Agent bestAgent;
        private final DijkstraAlgorithm.DijkstraResult routeToRestaurant;

        public AssignmentResult(Agent bestAgent, DijkstraAlgorithm.DijkstraResult routeToRestaurant) {
            this.bestAgent = bestAgent;
            this.routeToRestaurant = routeToRestaurant;
        }

        public Agent getBestAgent() { return bestAgent; }
        public DijkstraAlgorithm.DijkstraResult getRouteToRestaurant() { return routeToRestaurant; }
    }

    /**
     * Assigns the best available agent to a restaurant node using greedy selection.
     *
     * <p>Algorithm:
     * 1. For each available agent, compute Dijkstra(agent.currentNode → restaurantNode)
     * 2. Track the agent with the minimum travel time
     * 3. Return the best agent and their route</p>
     *
     * @param graph           the city graph
     * @param availableAgents list of available agents (must not be empty)
     * @param restaurantNode  the restaurant node to assign an agent to
     * @return AssignmentResult with the best agent and route, or null if no agent can reach the restaurant
     */
    public AssignmentResult assignBestAgent(Graph graph, List<Agent> availableAgents, int restaurantNode) {
        if (availableAgents == null || availableAgents.isEmpty()) {
            return null;
        }

        Agent bestAgent = null;
        DijkstraAlgorithm.DijkstraResult bestRoute = null;
        int minTravelTime = Integer.MAX_VALUE;

        for (Agent agent : availableAgents) {
            try {
                DijkstraAlgorithm.DijkstraResult result =
                        dijkstra.findShortestPath(graph, agent.getCurrentNode(), restaurantNode);

                if (result != null && result.getTotalDistance() < minTravelTime) {
                    minTravelTime = result.getTotalDistance();
                    bestAgent = agent;
                    bestRoute = result;
                }
            } catch (IllegalArgumentException e) {
                // Agent's node not in graph; skip this agent
                continue;
            }
        }

        if (bestAgent == null) {
            return null;
        }

        return new AssignmentResult(bestAgent, bestRoute);
    }
}
