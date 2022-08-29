package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) { this.userService = userService; }

    @PostMapping
    @ApiOperation(
            value = "Registers new user",
            notes = "Provide account information in the request body to create a new user account"
    )
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) throws AccountException {
        this.userService.registerBarOwner(userRegistrationRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
