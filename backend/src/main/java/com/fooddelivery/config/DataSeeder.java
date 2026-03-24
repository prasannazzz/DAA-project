package com.fooddelivery.config;

import com.fooddelivery.model.Agent;
import com.fooddelivery.model.AgentStatus;
import com.fooddelivery.model.Edge;
import com.fooddelivery.model.Node;
import com.fooddelivery.repository.AgentRepository;
import com.fooddelivery.repository.GraphRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Seeds the in-memory stores with a sample city graph on application startup.
 * Creates a 10-node city with ~18 directed edges and 3 pre-registered agents.
 */
@Component
public class DataSeeder {

    private final GraphRepository graphRepository;
    private final AgentRepository agentRepository;

    public DataSeeder(GraphRepository graphRepository, AgentRepository agentRepository) {
        this.graphRepository = graphRepository;
        this.agentRepository = agentRepository;
    }

    /**
     * Seeds sample data after Spring context is initialized.
     */
    @PostConstruct
    public void seed() {
        seedNodes();
        seedEdges();
        seedAgents();
    }

    private void seedNodes() {
        Node[] nodes = {
            new Node(1,  "Central Hub",      12.9716, 77.5946),
            new Node(2,  "Pizza Palace",     12.9750, 77.5900),
            new Node(3,  "Burger Barn",      12.9680, 77.5980),
            new Node(4,  "Sushi Station",    12.9800, 77.6000),
            new Node(5,  "Taco Town",        12.9650, 77.5850),
            new Node(6,  "Residential North", 12.9850, 77.5920),
            new Node(7,  "Residential South", 12.9600, 77.5950),
            new Node(8,  "Business District", 12.9720, 77.6050),
            new Node(9,  "Mall Area",         12.9780, 77.5850),
            new Node(10, "University Zone",   12.9630, 77.6020)
        };

        for (Node node : nodes) {
            graphRepository.addNodeMetadata(node);
        }
    }

    private void seedEdges() {
        // Directed edges with travel time in minutes
        int[][] edgeData = {
            {1, 2, 5},   // Central Hub → Pizza Palace
            {1, 3, 7},   // Central Hub → Burger Barn
            {1, 8, 4},   // Central Hub → Business District
            {2, 4, 6},   // Pizza Palace → Sushi Station
            {2, 9, 3},   // Pizza Palace → Mall Area
            {3, 5, 4},   // Burger Barn → Taco Town
            {3, 7, 5},   // Burger Barn → Residential South
            {3, 10, 6},  // Burger Barn → University Zone
            {4, 6, 3},   // Sushi Station → Residential North
            {4, 8, 5},   // Sushi Station → Business District
            {5, 7, 3},   // Taco Town → Residential South
            {5, 1, 8},   // Taco Town → Central Hub
            {6, 9, 4},   // Residential North → Mall Area
            {6, 2, 6},   // Residential North → Pizza Palace
            {7, 10, 4},  // Residential South → University Zone
            {8, 4, 3},   // Business District → Sushi Station
            {9, 6, 5},   // Mall Area → Residential North
            {10, 8, 7}   // University Zone → Business District
        };

        for (int[] e : edgeData) {
            graphRepository.getGraph().addEdge(new Edge(e[0], e[1], e[2]));
        }
    }

    private void seedAgents() {
        agentRepository.save(new Agent("agent-1", "Ravi Kumar",    1, AgentStatus.AVAILABLE));
        agentRepository.save(new Agent("agent-2", "Priya Sharma",  5, AgentStatus.AVAILABLE));
        agentRepository.save(new Agent("agent-3", "Amit Patel",    8, AgentStatus.AVAILABLE));
    }
}
