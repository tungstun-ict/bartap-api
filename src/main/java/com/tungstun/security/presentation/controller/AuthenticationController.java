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
@RequestMapping("/api")
public class AuthenticationController {
    private final UserService USER_SERVICE;

    public AuthenticationController(UserService userService) { this.USER_SERVICE = userService; }

    @PostMapping("/authenticate")
    @ApiOperation(
            value = "Logs in user",
            notes = "Provide login credentials in the request body to receive an access and refresh token"
    )
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) throws LoginException {
        Map<String, String> authorization = this.USER_SERVICE.loginUser(loginRequest);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(authorization);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(null);
    }

    @PostMapping("/authenticate/refresh")
    @ApiOperation(
            value = "Refreshes the user's access token",
            notes = "Provide refresh token, access token in the request body to receive a new access token"
    )
    public ResponseEntity<Void> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        Map<String, String> authorization = this.USER_SERVICE.refreshUserToken(refreshTokenRequest);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(authorization);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(null);
    }
}
