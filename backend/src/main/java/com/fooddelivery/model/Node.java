package com.fooddelivery.model;

/**
 * Represents a node (location) in the city graph.
 * Each node has a unique ID, a human-readable name, and geographic coordinates.
 */
public class Node {

    private int id;
    private String name;
    private double lat;
    private double lng;

    public Node() {}

    public Node(int id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    // --- Getters & Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    @Override
    public String toString() {
        return "Node{id=" + id + ", name='" + name + "'}";
    }
}
