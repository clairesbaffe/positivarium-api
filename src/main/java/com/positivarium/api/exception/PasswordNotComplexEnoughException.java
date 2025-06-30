package com.positivarium.api.exception;

public class PasswordNotComplexEnoughException extends RuntimeException {
    public PasswordNotComplexEnoughException(String message) {
        super(message);
    }
}
