package com.fooddelivery.controller;

import com.fooddelivery.dto.AssignmentResponse;
import com.fooddelivery.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for triggering agent-to-order assignments.
 */
@RestController
@RequestMapping("/api/assign")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * Triggers greedy agent assignment for the next pending order.
     *
     * @return assignment details including agent, route, and ETA
     */
    @PostMapping
    public ResponseEntity<AssignmentResponse> assignOrder() {
        AssignmentResponse response = assignmentService.assignNextOrder();
        return ResponseEntity.ok(response);
    }
}
