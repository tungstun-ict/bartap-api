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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    private final UserService USER_SERVICE;

    public RegistrationController(UserService userService) { this.USER_SERVICE = userService; }

    @PostMapping
    @ApiOperation(
            value = "Registers new user",
            notes = "Provide account information in the request body to create a new user account"
    )
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) {
        this.USER_SERVICE.registerBarOwner(
                userRegistrationRequest.username,
                userRegistrationRequest.password
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public void login(
//            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) {
        System.out.println("test");
//        this.USER_SERVICE.registerBarOwner(
//                userRegistrationRequest.username,
//                userRegistrationRequest.password
//        );
//        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
