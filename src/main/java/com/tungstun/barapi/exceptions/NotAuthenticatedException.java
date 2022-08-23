package com.tungstun.barapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthenticatedException extends AuthenticationException {
    public NotAuthenticatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NotAuthenticatedException(String msg) {
        super(msg);
    }
}
