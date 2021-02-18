package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserService USER_SERVICE;

    public RegistrationController(UserService userService) { this.USER_SERVICE = userService; }

    @PostMapping
    public ResponseEntity<Void> register(
             @RequestBody UserRegistrationRequest userRegistrationRequest
    ) {
        this.USER_SERVICE.registerBarOwner(
                userRegistrationRequest.username,
                userRegistrationRequest.password
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
