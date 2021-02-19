package com.tungstun.barapi.exceptions;

public class AlreadyActiveSessionException extends RuntimeException {
    private final String message;

    public AlreadyActiveSessionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() { return this.message; }
}
