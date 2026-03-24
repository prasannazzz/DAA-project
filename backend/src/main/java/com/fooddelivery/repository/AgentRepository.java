package com.fooddelivery.repository;

import com.fooddelivery.model.Agent;
import com.fooddelivery.model.AgentStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for delivery agents.
 */
@Repository
public class AgentRepository {

    private final Map<String, Agent> agents = new ConcurrentHashMap<>();

    /**
     * Adds or updates an agent.
     *
     * @param agent the agent to save
     */
    public void save(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    /**
     * Finds an agent by ID.
     *
     * @param id agent ID
     * @return the agent, or null if not found
     */
    public Agent findById(String id) {
        return agents.get(id);
    }

    /**
     * Returns all agents.
     *
     * @return list of all agents
     */
    public List<Agent> findAll() {
        return new ArrayList<>(agents.values());
    }

    /**
     * Returns all agents with AVAILABLE status.
     *
     * @return list of available agents
     */
    public List<Agent> findAvailable() {
        return agents.values().stream()
                .filter(a -> a.getStatus() == AgentStatus.AVAILABLE)
                .toList();
    }

    /**
     * Updates an agent's status.
     *
     * @param id     agent ID
     * @param status new status
     */
    public void updateStatus(String id, AgentStatus status) {
        Agent agent = agents.get(id);
        if (agent != null) {
            agent.setStatus(status);
        }
    }

    /**
     * Updates an agent's current node position.
     *
     * @param id   agent ID
     * @param node new node position
     */
    public void updateLocation(String id, int node) {
        Agent agent = agents.get(id);
        if (agent != null) {
            agent.setCurrentNode(node);
        }
    }
}
