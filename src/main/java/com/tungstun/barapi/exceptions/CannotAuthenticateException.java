package com.tungstun.barapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CannotAuthenticateException extends AuthenticationException {
    public CannotAuthenticateException(String msg) {
        super(msg);
    }
}
