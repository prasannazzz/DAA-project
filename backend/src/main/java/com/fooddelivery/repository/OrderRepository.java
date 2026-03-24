package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for orders using a PriorityQueue (min-heap).
 * Orders are prioritized by priority value (ascending), then by timestamp (ascending).
 */
@Repository
public class OrderRepository {

    /** Min-heap: lower priority number and earlier timestamp come first */
    private final PriorityQueue<Order> orderQueue = new PriorityQueue<>(
            Comparator.comparingInt(Order::getPriority)
                    .thenComparingLong(Order::getTimestamp)
    );

    /** Map for O(1) lookup by order ID */
    private final Map<String, Order> orderMap = new ConcurrentHashMap<>();

    /**
     * Adds an order to the priority queue and lookup map.
     *
     * @param order the order to add
     */
    public synchronized void addOrder(Order order) {
        orderQueue.offer(order);
        orderMap.put(order.getId(), order);
    }

    /**
     * Returns and removes the highest-priority order from the queue.
     *
     * @return the highest-priority order, or null if queue is empty
     */
    public synchronized Order pollHighestPriority() {
        Order order = orderQueue.poll();
        if (order != null) {
            orderMap.remove(order.getId());
        }
        return order;
    }

    /**
     * Returns the highest-priority order without removing it.
     *
     * @return the highest-priority order, or null if queue is empty
     */
    public synchronized Order peekHighestPriority() {
        return orderQueue.peek();
    }

    /**
     * Returns all orders sorted by priority (ascending), then timestamp.
     *
     * @return sorted list of all orders
     */
    public synchronized List<Order> getAllOrdersSorted() {
        List<Order> sorted = new ArrayList<>(orderMap.values());
        sorted.sort(Comparator.comparingInt(Order::getPriority)
                .thenComparingLong(Order::getTimestamp));
        return sorted;
    }

    /**
     * Returns all pending orders (status = PENDING).
     *
     * @return list of pending orders sorted by priority
     */
    public synchronized List<Order> getPendingOrders() {
        return orderMap.values().stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .sorted(Comparator.comparingInt(Order::getPriority)
                        .thenComparingLong(Order::getTimestamp))
                .toList();
    }

    /**
     * Finds an order by ID.
     *
     * @param id order ID
     * @return the order, or null if not found
     */
    public Order findById(String id) {
        return orderMap.get(id);
    }

    /**
     * Updates an order's status. Removes and re-adds to maintain queue consistency.
     *
     * @param id     order ID
     * @param status new status
     */
    public synchronized void updateStatus(String id, String status) {
        Order order = orderMap.get(id);
        if (order != null) {
            orderQueue.remove(order);
            order.setStatus(status);
            orderQueue.offer(order);
        }
    }

    /**
     * Returns the number of orders in the repository.
     *
     * @return order count
     */
    public int size() {
        return orderMap.size();
    }
}
