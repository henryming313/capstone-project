package com.centria.cabbooking.exception;

/**
 * Custom exception for resource conflict scenarios (e.g., duplicate email/mobile)
 */
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}