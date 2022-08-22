package com.tungstun.exception;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAuthorizedException extends AccessDeniedException {
    public NotAuthorizedException(String msg) {
        super(msg);
    }
}
