package com.fooddelivery.service;

import com.fooddelivery.dto.AgentRequest;
import com.fooddelivery.model.Agent;
import com.fooddelivery.model.AgentStatus;
import com.fooddelivery.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing delivery agents.
 */
@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /**
     * Registers a new delivery agent. Defaults to AVAILABLE status.
     *
     * @param request agent registration details
     * @return the created Agent
     */
    public Agent registerAgent(AgentRequest request) {
        Agent agent = new Agent(
                "AGT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                request.getName(),
                request.getCurrentNode(),
                AgentStatus.AVAILABLE
        );
        agentRepository.save(agent);
        return agent;
    }

    /**
     * Returns all agents.
     *
     * @return list of all agents
     */
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    /**
     * Returns all available agents.
     *
     * @return list of agents with AVAILABLE status
     */
    public List<Agent> getAvailableAgents() {
        return agentRepository.findAvailable();
    }

    /**
     * Marks an agent as BUSY.
     *
     * @param agentId the agent ID
     */
    public void markBusy(String agentId) {
        agentRepository.updateStatus(agentId, AgentStatus.BUSY);
    }

    /**
     * Marks an agent as AVAILABLE and updates their location.
     *
     * @param agentId agent ID
     * @param nodeId  new node position
     */
    public void markAvailable(String agentId, int nodeId) {
        agentRepository.updateStatus(agentId, AgentStatus.AVAILABLE);
        agentRepository.updateLocation(agentId, nodeId);
    }

    /**
     * Finds an agent by ID.
     *
     * @param id agent ID
     * @return the agent, or null
     */
    public Agent findById(String id) {
        return agentRepository.findById(id);
    }
}
