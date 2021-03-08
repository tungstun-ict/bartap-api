package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final UserService USER_SERVICE;

    public AuthenticationController(UserService userService) { this.USER_SERVICE = userService; }

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
