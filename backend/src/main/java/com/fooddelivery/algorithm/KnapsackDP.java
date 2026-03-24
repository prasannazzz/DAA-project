package com.fooddelivery.algorithm;

import com.fooddelivery.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the 0/1 Knapsack algorithm using Dynamic Programming for order batching.
 *
 * <p>Use case: Given an agent with a limited time capacity (knapsack size),
 * select the optimal subset of orders to deliver that maximizes total priority value
 * within the time constraint.</p>
 *
 * <p>Each order is treated as an item where:
 * - Weight = estimated delivery time (derived from route distance)
 * - Value  = order priority (higher priority = more valuable to deliver first)</p>
 *
 * <p><b>Time Complexity:</b> O(n × W) where n = number of orders, W = capacity.</p>
 * <p><b>Space Complexity:</b> O(n × W) for the DP table.</p>
 */
public class KnapsackDP {

    /**
     * Result holder for knapsack optimization.
     */
    public static class KnapsackResult {
        private final List<Order> selectedOrders;
        private final int totalValue;
        private final int totalWeight;

        public KnapsackResult(List<Order> selectedOrders, int totalValue, int totalWeight) {
            this.selectedOrders = selectedOrders;
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
        }

        public List<Order> getSelectedOrders() { return selectedOrders; }
        public int getTotalValue() { return totalValue; }
        public int getTotalWeight() { return totalWeight; }
    }

    /**
     * Selects the optimal batch of orders using 0/1 Knapsack DP.
     *
     * <p>Algorithm:
     * 1. Build a 2D DP table where dp[i][w] = max priority achievable
     *    using first i orders with capacity w
     * 2. Fill the table bottom-up
     * 3. Backtrack to find which orders were selected</p>
     *
     * @param orders   list of candidate orders
     * @param weights  delivery time for each order (parallel array with orders)
     * @param capacity maximum time capacity of the agent
     * @return KnapsackResult with selected orders, total value, and total weight
     */
    public KnapsackResult optimize(List<Order> orders, int[] weights, int capacity) {
        int n = orders.size();

        if (n == 0 || capacity <= 0) {
            return new KnapsackResult(new ArrayList<>(), 0, 0);
        }

        // Values: use inverse priority (lower priority number = higher value)
        // so priority 1 has value 10, priority 5 has value 6, etc.
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = Math.max(1, 11 - orders.get(i).getPriority());
        }

        // DP table: dp[i][w] = max value using items 0..i-1 with capacity w
        int[][] dp = new int[n + 1][capacity + 1];

        // Fill DP table bottom-up
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                // Option 1: Don't include item i-1
                dp[i][w] = dp[i - 1][w];

                // Option 2: Include item i-1 (if it fits)
                if (weights[i - 1] <= w) {
                    int includeValue = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                    dp[i][w] = Math.max(dp[i][w], includeValue);
                }
            }
        }

        // Backtrack to find selected orders
        List<Order> selectedOrders = new ArrayList<>();
        int totalWeight = 0;
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                // Item i-1 was included
                selectedOrders.add(orders.get(i - 1));
                totalWeight += weights[i - 1];
                w -= weights[i - 1];
            }
        }

        return new KnapsackResult(selectedOrders, dp[n][capacity], totalWeight);
    }
}
