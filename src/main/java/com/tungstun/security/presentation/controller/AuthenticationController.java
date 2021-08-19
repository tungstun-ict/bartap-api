package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.presentation.dto.request.LoginRequest;
import com.tungstun.security.presentation.dto.request.RefreshTokenRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    private ResponseEntity<Void> createResponseWithHeaders(Map<String, String> headers) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(headers);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping
    @ApiOperation(
            value = "Logs in user",
            notes = "Provide login credentials in the request body to receive an access and refresh token"
    )
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) throws LoginException {
        Map<String, String> authorization = this.userService.loginUser(loginRequest);
        return createResponseWithHeaders(authorization);
    }

    @PostMapping("/refresh")
    @ApiOperation(
            value = "Refreshes the user's access token",
            notes = "Provide refresh token, access token in the request body to receive a new access token"
    )
    public ResponseEntity<Void> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, String> authorization = this.userService.refreshUserToken(refreshTokenRequest);
        return createResponseWithHeaders(authorization);
    }
}
