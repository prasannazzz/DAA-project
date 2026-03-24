package com.fooddelivery.dto;

import java.util.List;

/**
 * Response DTO for shortest route computation results.
 */
public class RouteResponse {

    private List<Integer> path;
    private int totalDistance;
    private List<String> pathNames;

    public RouteResponse() {}

    public RouteResponse(List<Integer> path, int totalDistance, List<String> pathNames) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.pathNames = pathNames;
    }

    public List<Integer> getPath() { return path; }
    public void setPath(List<Integer> path) { this.path = path; }

    public int getTotalDistance() { return totalDistance; }
    public void setTotalDistance(int totalDistance) { this.totalDistance = totalDistance; }

    public List<String> getPathNames() { return pathNames; }
    public void setPathNames(List<String> pathNames) { this.pathNames = pathNames; }
}
