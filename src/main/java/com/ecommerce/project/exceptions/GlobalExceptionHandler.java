package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class centralizes exception handling using Spring's {@link RestControllerAdvice}.
 * Provides custom responses for various exceptions to ensure consistency in API error handling.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown by Spring when request body validation fails.
     * Captures field-specific error messages and returns them in a map.
     *
     * @param e The {@link MethodArgumentNotValidException} containing validation errors.
     * @return A {@link ResponseEntity} with a map of field errors and corresponding messages, along with a BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(err -> {
                    String fieldName = ((FieldError) err).getField();
                    String message = err.getDefaultMessage();
                    response.put(fieldName, message);
                });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link ResourceNotFoundException}.
     * This is triggered when a requested resource cannot be found in the database.
     *
     * @param e The {@link ResourceNotFoundException} containing details of the missing resource.
     * @return A {@link ResponseEntity} containing an {@link APIResponse} with a NOT_FOUND status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link APIException}.
     * This is used to handle application-specific exceptions for API errors.
     *
     * @param e The {@link APIException} containing a custom error message.
     * @return A {@link ResponseEntity} containing an {@link APIResponse} with a BAD_REQUEST status.
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> APIExceptionHandler(APIException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
