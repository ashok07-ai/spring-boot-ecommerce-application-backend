package com.ecommerce.project.exceptions;

import java.io.Serial;

/**
 * Custom exception class for handling API-related errors in the application.
 * Extends {@link RuntimeException}, making it an unchecked exception.
 * This can be used to represent application-specific errors during API operations.
 */
public class APIException extends RuntimeException {

    // A unique identifier for the serialized object, ensuring compatibility during serialization/deserialization
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for APIException.
     * Creates an instance of APIException without any error message.
     */
    public APIException() {
        // Default constructor
    }

    /**
     * Parameterized constructor for APIException.
     * Allows creating an exception instance with a custom error message.
     *
     * @param message The detailed error message for the exception.
     */
    public APIException(String message) {
        super(message);
    }
}
