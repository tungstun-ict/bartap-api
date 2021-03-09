package com.tungstun.barapi.exceptions;

public class LockedSessionException extends RuntimeException {
    public LockedSessionException() { }

    public LockedSessionException(String message) {
        super(message);
    }
}
