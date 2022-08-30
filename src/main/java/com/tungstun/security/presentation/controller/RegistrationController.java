package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.user.UserCommandHandler;
import com.tungstun.security.application.user.command.RegisterUser;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    private final UserCommandHandler userCommandHandler;

    public RegistrationController(UserCommandHandler userCommandHandler) { this.userCommandHandler = userCommandHandler; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Registers new user",
            notes = "Provide account information in the request body to create a new user account"
    )
    public Long register(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) throws AccountException {
        RegisterUser command = new RegisterUser(
                userRegistrationRequest.username,
                userRegistrationRequest.password,
                userRegistrationRequest.firstName,
                userRegistrationRequest.lastName,
                userRegistrationRequest.mail,
                userRegistrationRequest.phoneNumber
        );
        return userCommandHandler.registerUser(command);
    }
}
