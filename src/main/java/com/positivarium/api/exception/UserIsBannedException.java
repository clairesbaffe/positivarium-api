package com.positivarium.api.exception;

public class UserIsBannedException extends RuntimeException {
    public UserIsBannedException(String message) {
        super(message);
    }
}
