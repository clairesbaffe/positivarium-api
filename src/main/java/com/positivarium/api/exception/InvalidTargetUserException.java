package com.positivarium.api.exception;

public class InvalidTargetUserException extends RuntimeException {
    public InvalidTargetUserException(String message) {
        super(message);
    }
}
