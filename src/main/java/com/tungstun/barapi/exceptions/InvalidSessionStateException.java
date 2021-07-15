package com.tungstun.barapi.exceptions;

public class InvalidSessionStateException extends RuntimeException {
    public InvalidSessionStateException() { }

    public InvalidSessionStateException(String message) {
        super(message);
    }
}
