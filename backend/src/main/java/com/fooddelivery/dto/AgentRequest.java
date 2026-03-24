package com.fooddelivery.dto;

/**
 * Request DTO for registering a new delivery agent.
 */
public class AgentRequest {

    private String name;
    private int currentNode;

    public AgentRequest() {}

    public AgentRequest(String name, int currentNode) {
        this.name = name;
        this.currentNode = currentNode;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCurrentNode() { return currentNode; }
    public void setCurrentNode(int currentNode) { this.currentNode = currentNode; }
}
