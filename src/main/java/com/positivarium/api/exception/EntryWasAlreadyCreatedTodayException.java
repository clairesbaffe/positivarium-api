package com.positivarium.api.exception;

public class EntryWasAlreadyCreatedTodayException extends RuntimeException {
    public EntryWasAlreadyCreatedTodayException() {
        super("An entry was already created today");
    }
}
