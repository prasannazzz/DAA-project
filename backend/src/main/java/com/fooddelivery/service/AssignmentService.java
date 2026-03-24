package com.fooddelivery.service;

import com.fooddelivery.algorithm.DijkstraAlgorithm.DijkstraResult;
import com.fooddelivery.algorithm.GreedyAssigner;
import com.fooddelivery.algorithm.GreedyAssigner.AssignmentResult;
import com.fooddelivery.dto.AssignmentResponse;
import com.fooddelivery.exception.ApiException;
import com.fooddelivery.model.Agent;
import com.fooddelivery.model.Node;
import com.fooddelivery.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for assigning agents to orders using the Greedy algorithm.
 * Integrates the full pipeline: PriorityQueue → Greedy → Dijkstra.
 */
@Service
public class AssignmentService {

    private final GreedyAssigner greedyAssigner = new GreedyAssigner();
    private final OrderService orderService;
    private final AgentService agentService;
    private final DijkstraService dijkstraService;
    private final GraphService graphService;

    public AssignmentService(OrderService orderService, AgentService agentService,
                             DijkstraService dijkstraService, GraphService graphService) {
        this.orderService = orderService;
        this.agentService = agentService;
        this.dijkstraService = dijkstraService;
        this.graphService = graphService;
    }

    /**
     * Assigns the best available agent to the next pending order.
     *
     * <p>Pipeline:
     * 1. Poll highest-priority order from the PriorityQueue
     * 2. Get available agents
     * 3. Run Greedy assignment (Dijkstra per agent) to find closest agent
     * 4. Compute full delivery route (agent → restaurant → customer)
     * 5. Update agent status to BUSY and order status to ASSIGNED</p>
     *
     * @return AssignmentResponse with assignment details
     * @throws ApiException if no pending orders or available agents
     */
    public AssignmentResponse assignNextOrder() {
        // Step 1: Get pending orders and pick the highest priority
        List<Order> pendingOrders = orderService.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            throw new ApiException("No pending orders to assign", HttpStatus.NOT_FOUND);
        }
        Order order = pendingOrders.get(0);

        // Step 2: Get available agents
        List<Agent> availableAgents = agentService.getAvailableAgents();
        if (availableAgents.isEmpty()) {
            throw new ApiException("No available agents for assignment", HttpStatus.NOT_FOUND);
        }

        // Step 3: Greedy assignment — find closest agent to restaurant
        AssignmentResult assignment = greedyAssigner.assignBestAgent(
                graphService.getGraph(), availableAgents, order.getRestaurantNode()
        );

        if (assignment == null) {
            throw new ApiException("No agent can reach restaurant node " + order.getRestaurantNode(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Step 4: Compute restaurant → customer route
        DijkstraResult deliveryRoute = dijkstraService.computePath(
                order.getRestaurantNode(), order.getCustomerNode()
        );

        // Step 5: Update statuses
        agentService.markBusy(assignment.getBestAgent().getId());
        orderService.updateOrderStatus(order.getId(), "ASSIGNED");

        // Build response
        AssignmentResponse response = new AssignmentResponse();
        response.setAgentId(assignment.getBestAgent().getId());
        response.setAgentName(assignment.getBestAgent().getName());
        response.setOrderId(order.getId());
        response.setRouteToRestaurant(assignment.getRouteToRestaurant().getPath());
        response.setTravelTimeToRestaurant(assignment.getRouteToRestaurant().getTotalDistance());

        if (deliveryRoute != null) {
            response.setRouteToCustomer(deliveryRoute.getPath());
            response.setTotalDeliveryTime(
                    assignment.getRouteToRestaurant().getTotalDistance() + deliveryRoute.getTotalDistance()
            );
        } else {
            response.setTotalDeliveryTime(assignment.getRouteToRestaurant().getTotalDistance());
        }

        Node restaurant = graphService.getNode(order.getRestaurantNode());
        Node customer = graphService.getNode(order.getCustomerNode());
        response.setMessage(String.format("Agent '%s' assigned to order %s. Picking up from %s, delivering to %s. ETA: %d min",
                assignment.getBestAgent().getName(), order.getId(),
                restaurant != null ? restaurant.getName() : "Node " + order.getRestaurantNode(),
                customer != null ? customer.getName() : "Node " + order.getCustomerNode(),
                response.getTotalDeliveryTime()));

        return response;
    }
}
