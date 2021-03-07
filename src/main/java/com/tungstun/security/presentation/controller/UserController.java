package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService USER_SERVICE;

    public UserController(UserService userService) { this.USER_SERVICE = userService; }

    @PostMapping("/register")
    @ApiOperation(
            value = "Registers new user",
            notes = "Provide account information in the request body to create a new user account"
    )
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) {
        this.USER_SERVICE.registerBarOwner(userRegistrationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ApiOperation(
            value = "Logs in user",
            notes = "Provide login credentials in the request body to receive a session token"
    )
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) throws LoginException {
        String jwt = this.USER_SERVICE.loginUser(loginRequest);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", jwt);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(null);
    }
}
