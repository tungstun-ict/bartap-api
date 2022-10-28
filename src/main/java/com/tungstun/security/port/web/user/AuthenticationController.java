package com.tungstun.security.port.web.user;

import com.tungstun.security.application.user.UserCommandHandler;
import com.tungstun.security.application.user.command.LogIn;
import com.tungstun.security.application.user.command.RefreshAccessToken;
import com.tungstun.security.port.web.user.request.LoginRequest;
import com.tungstun.security.port.web.user.request.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
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
    private final UserCommandHandler userCommandHandler;

    public AuthenticationController(UserCommandHandler userCommandHandler) {
        this.userCommandHandler = userCommandHandler;
    }

    private ResponseEntity<Void> createResponseWithHeaders(Map<String, String> headers) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAll(headers);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping
    @Operation(
            summary = "Logs in a user",
            description = "Log in using the provided login credentials and receive an access and refresh token",
            tags = "Account"
    )
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) throws LoginException {
        LogIn command = new LogIn(loginRequest.username(), loginRequest.password());
        Map<String, String> authorization = userCommandHandler.handle(command);
        return createResponseWithHeaders(authorization);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refreshes the user's access token",
            description = "Request a new access token using the old access token and the refresh token",
            tags = "Account"
    )
    public ResponseEntity<Void> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshAccessToken command = new RefreshAccessToken(refreshTokenRequest.accessToken(), refreshTokenRequest.refreshToken());
        Map<String, String> authorization = userCommandHandler.handle(command);
        return createResponseWithHeaders(authorization);
    }
}
