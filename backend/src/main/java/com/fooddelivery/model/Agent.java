package com.fooddelivery.model;

/**
 * Represents a delivery agent in the system.
 * Agents are positioned at graph nodes and have an availability status.
 */
public class Agent {

    private String id;
    private String name;
    private int currentNode;
    private AgentStatus status;

    public Agent() {}

    public Agent(String id, String name, int currentNode, AgentStatus status) {
        this.id = id;
        this.name = name;
        this.currentNode = currentNode;
        this.status = status;
    }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCurrentNode() { return currentNode; }
    public void setCurrentNode(int currentNode) { this.currentNode = currentNode; }

    public AgentStatus getStatus() { return status; }
    public void setStatus(AgentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Agent{id='" + id + "', name='" + name + "', node=" + currentNode + ", status=" + status + "}";
    }
}
