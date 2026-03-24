package com.fooddelivery.model;

/**
 * Represents a food delivery order.
 * Orders are prioritized by their priority value (lower = higher priority)
 * and then by timestamp (earlier = higher priority).
 */
public class Order {

    private String id;
    private int restaurantNode;
    private int customerNode;
    private int priority;
    private long timestamp;
    private String status; // PENDING, ASSIGNED, DELIVERED

    public Order() {}

    public Order(String id, int restaurantNode, int customerNode, int priority, long timestamp, String status) {
        this.id = id;
        this.restaurantNode = restaurantNode;
        this.customerNode = customerNode;
        this.priority = priority;
        this.timestamp = timestamp;
        this.status = status;
    }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getRestaurantNode() { return restaurantNode; }
    public void setRestaurantNode(int restaurantNode) { this.restaurantNode = restaurantNode; }

    public int getCustomerNode() { return customerNode; }
    public void setCustomerNode(int customerNode) { this.customerNode = customerNode; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{id='" + id + "', restaurant=" + restaurantNode + ", customer=" + customerNode
                + ", priority=" + priority + ", status='" + status + "'}";
    }
}
