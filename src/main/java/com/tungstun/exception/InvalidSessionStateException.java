package com.tungstun.exception;

public class InvalidSessionStateException extends RuntimeException {
    public InvalidSessionStateException() { }

    public InvalidSessionStateException(String message) {
        super(message);
    }
}
