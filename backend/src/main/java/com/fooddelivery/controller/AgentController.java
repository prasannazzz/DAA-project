package com.fooddelivery.controller;

import com.fooddelivery.dto.AgentRequest;
import com.fooddelivery.model.Agent;
import com.fooddelivery.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for delivery agent management.
 */
@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * Registers a new delivery agent.
     *
     * @param request agent details (name, currentNode)
     * @return the registered agent with 201 status
     */
    @PostMapping
    public ResponseEntity<Agent> registerAgent(@RequestBody AgentRequest request) {
        Agent agent = agentService.registerAgent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(agent);
    }

    /**
     * Returns all agents.
     *
     * @return list of all agents
     */
    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }

    /**
     * Returns all available (non-busy) agents.
     *
     * @return list of available agents
     */
    @GetMapping("/available")
    public ResponseEntity<List<Agent>> getAvailableAgents() {
        return ResponseEntity.ok(agentService.getAvailableAgents());
    }
}
