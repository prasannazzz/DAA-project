package com.fooddelivery.service;

import com.fooddelivery.dto.OrderRequest;
import com.fooddelivery.model.Order;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing delivery orders.
 * Orders are stored in a priority queue (min-heap) for scheduling.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Places a new delivery order.
     * Generates a unique ID and timestamps the order.
     *
     * @param request order details from the client
     * @return the created Order
     */
    public Order placeOrder(OrderRequest request) {
        Order order = new Order(
                "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                request.getRestaurantNode(),
                request.getCustomerNode(),
                request.getPriority(),
                System.currentTimeMillis(),
                "PENDING"
        );
        orderRepository.addOrder(order);
        return order;
    }

    /**
     * Returns all orders sorted by priority (ascending) then timestamp.
     *
     * @return sorted list of all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrdersSorted();
    }

    /**
     * Returns all pending orders.
     *
     * @return list of pending orders
     */
    public List<Order> getPendingOrders() {
        return orderRepository.getPendingOrders();
    }

    /**
     * Polls the highest-priority pending order from the queue.
     *
     * @return the highest-priority order, or null if none
     */
    public Order pollNextOrder() {
        return orderRepository.pollHighestPriority();
    }

    /**
     * Updates an order's status.
     *
     * @param orderId order ID
     * @param status  new status
     */
    public void updateOrderStatus(String orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }

    /**
     * Finds an order by ID.
     *
     * @param id order ID
     * @return the order, or null
     */
    public Order findById(String id) {
        return orderRepository.findById(id);
    }
}
