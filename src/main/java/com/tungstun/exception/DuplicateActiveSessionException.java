package com.tungstun.exception;

public class DuplicateActiveSessionException extends RuntimeException {
    private final String message;

    public DuplicateActiveSessionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() { return this.message; }
}
