package com.fitness.fitnesstrackerapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String objectName, Object id) {
        super(resourceName + " not found with " + id);
    }
}
