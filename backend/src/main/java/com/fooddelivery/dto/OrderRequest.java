package com.fooddelivery.dto;

/**
 * Request DTO for placing a new delivery order.
 */
public class OrderRequest {

    private int restaurantNode;
    private int customerNode;
    private int priority;

    public OrderRequest() {}

    public OrderRequest(int restaurantNode, int customerNode, int priority) {
        this.restaurantNode = restaurantNode;
        this.customerNode = customerNode;
        this.priority = priority;
    }

    public int getRestaurantNode() { return restaurantNode; }
    public void setRestaurantNode(int restaurantNode) { this.restaurantNode = restaurantNode; }

    public int getCustomerNode() { return customerNode; }
    public void setCustomerNode(int customerNode) { this.customerNode = customerNode; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
