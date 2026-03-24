package com.fooddelivery.exception;

import com.fooddelivery.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler that converts exceptions to structured API error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom ApiException instances.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {
        ApiErrorResponse error = new ApiErrorResponse(ex.getStatus().value(), ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    /**
     * Handles IllegalArgumentException instances.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ApiErrorResponse error = new ApiErrorResponse(400, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Fallback handler for unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex) {
        ApiErrorResponse error = new ApiErrorResponse(500, "Internal server error: " + ex.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}
