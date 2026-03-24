package com.fooddelivery.model;

/**
 * Represents a directed edge in the city graph.
 * Weight represents travel time in minutes (non-negative).
 */
public class Edge {

    private int from;
    private int to;
    private int weight;

    public Edge() {}

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    // --- Getters & Setters ---

    public int getFrom() { return from; }
    public void setFrom(int from) { this.from = from; }

    public int getTo() { return to; }
    public void setTo(int to) { this.to = to; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() {
        return "Edge{" + from + " → " + to + ", weight=" + weight + "}";
    }
}
