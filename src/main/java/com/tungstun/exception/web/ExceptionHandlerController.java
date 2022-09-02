package com.tungstun.exception.web;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.exception.NotAuthenticatedException;
import com.tungstun.exception.NotAuthorizedException;
import com.tungstun.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.directory.InvalidAttributesException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

/**
 * Base exception handler class for bartap controllers<br>
 * This handler handles all basic and shared exceptions that could be thrown.<br>
 */
@ControllerAdvice
@RestController
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {NotAuthenticatedException.class})
    public ExceptionResponse handleNotAuthenticatedExceptions(NotAuthenticatedException e) {
        return ExceptionResponse.with("User not authenticated", e.getLocalizedMessage());
    }

    @ExceptionHandler(value = {NotAuthorizedException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleNotAuthorizedExceptions(RuntimeException e) {
        return ExceptionResponse.with("User not authorized for action or resource", e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EntityNotFoundException.class,})
    public ExceptionResponse mainHandler(Exception e) {
        return ExceptionResponse.with("Error", e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {
            InvalidAttributesException.class,
            DuplicateRequestException.class
    })
    public ExceptionResponse invalidArgsHandler(RuntimeException e) {
        return ExceptionResponse.with("Error", e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ExceptionResponse handleConstraintViolations(ConstraintViolationException e) {
        List<String> violationMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return ExceptionResponse.with("Incorrect input", violationMessages);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ExceptionResponse handleUserNotFoundException(UserNotFoundException e) {
        return ExceptionResponse.with("User not found", e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {LoginException.class})
    public ExceptionResponse handleLoginException(LoginException e) {
        return ExceptionResponse.with("Login failed", e.getLocalizedMessage());
    }



    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ExceptionResponse handleConstraintViolations(DataIntegrityViolationException e) {
        LOG.error(Arrays.toString(e.getStackTrace()));
        return ExceptionResponse.with("Incorrect input", List.of("Something went wrong during persistence"));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {AccountException.class})
    public ExceptionResponse handleAccountException(AccountException e) {
        LOG.error(Arrays.toString(e.getStackTrace()));
        return ExceptionResponse.with("Account exception", e.getLocalizedMessage());
    }


    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ExceptionResponse.with("Unexpected error", e.getMessage());
    }
}
