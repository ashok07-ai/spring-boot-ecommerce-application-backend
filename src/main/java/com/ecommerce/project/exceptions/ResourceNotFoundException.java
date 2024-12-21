package com.ecommerce.project.exceptions;

/**
 * Custom exception to handle scenarios where a requested resource cannot be found.
 * Extends {@link RuntimeException} to provide runtime exception capabilities.
 * Supports dynamic exception messages for better debugging and user feedback.
 */
public class ResourceNotFoundException extends RuntimeException {
    // The name of the resource that could not be found
    private String resourceName;

    // The name of the field used to search for the resource
    private String field;

    // The value of the field used to search for the resource (String version)
    private String fieldName;

    // The value of the field used to search for the resource (Long version)
    private Long fieldId;

    /**
     * Default constructor for generic usage.
     */
    public ResourceNotFoundException() {
    }

    /**
     * Constructor to create an exception with a message for a missing resource identified by a string field.
     *
     * @param resourceName The name of the resource that could not be found.
     * @param field        The name of the field used in the search query.
     * @param fieldName    The value of the field used in the search query (as a string).
     */
    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    /**
     * Constructor to create an exception with a message for a missing resource identified by a numeric field.
     *
     * @param resourceName The name of the resource that could not be found.
     * @param field        The name of the field used in the search query.
     * @param fieldId      The numeric value of the field used in the search query.
     */
    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
