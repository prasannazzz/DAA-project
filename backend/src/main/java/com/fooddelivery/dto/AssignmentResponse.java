package com.fooddelivery.dto;

import com.fooddelivery.model.Agent;
import com.fooddelivery.model.Order;
import java.util.List;

/**
 * Response DTO for agent-to-order assignment results.
 */
public class AssignmentResponse {

    private String agentId;
    private String agentName;
    private String orderId;
    private int travelTimeToRestaurant;
    private List<Integer> routeToRestaurant;
    private int totalDeliveryTime;
    private List<Integer> routeToCustomer;
    private String message;

    public AssignmentResponse() {}

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getTravelTimeToRestaurant() { return travelTimeToRestaurant; }
    public void setTravelTimeToRestaurant(int travelTimeToRestaurant) { this.travelTimeToRestaurant = travelTimeToRestaurant; }

    public List<Integer> getRouteToRestaurant() { return routeToRestaurant; }
    public void setRouteToRestaurant(List<Integer> routeToRestaurant) { this.routeToRestaurant = routeToRestaurant; }

    public int getTotalDeliveryTime() { return totalDeliveryTime; }
    public void setTotalDeliveryTime(int totalDeliveryTime) { this.totalDeliveryTime = totalDeliveryTime; }

    public List<Integer> getRouteToCustomer() { return routeToCustomer; }
    public void setRouteToCustomer(List<Integer> routeToCustomer) { this.routeToCustomer = routeToCustomer; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
