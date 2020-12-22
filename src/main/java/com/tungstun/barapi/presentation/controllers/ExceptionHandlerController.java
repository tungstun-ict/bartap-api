package com.tungstun.barapi.presentation.controllers;

import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles Not Found Exceptions*/
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Map<String, String>> nfHandler(NotFoundException nf) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  nf.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateRequestException.class)
    public ResponseEntity<Map<String, String>> drHandler(DuplicateRequestException dr) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  dr.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other Exceptions to avoid crashes*/
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> e(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Unknown Error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}
