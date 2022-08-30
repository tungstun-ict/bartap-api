package com.tungstun.barapi.exceptions;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.directory.InvalidAttributesException;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles self specified exceptions*/
    @ExceptionHandler(
            value = {
                    EntityNotFoundException.class,
            }
    )
    public ResponseEntity<Map<String, String>> mainHandler(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles invalid argument input / request exceptions*/
    @ExceptionHandler(
            value = {
                    InvalidAttributesException.class,
                    DuplicateRequestException.class
            }
    )
    public ResponseEntity<Map<String, String>> invalidArgsHandler(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    /**
     * Handles security exceptions*/
    @ExceptionHandler(
            value = {
                    AccessDeniedException.class,
            }
    )
    public ResponseEntity<Map<String, String>> securityAccessHandler(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles all other Exceptions to avoid crashes*/
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> e(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        e.printStackTrace();
        map.put("Unknown Error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> e(RuntimeException e) {
        HashMap<String, String> map = new HashMap<>();
        e.printStackTrace();
        map.put("Unknown Error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}
