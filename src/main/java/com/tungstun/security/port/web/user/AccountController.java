package com.tungstun.security.port.web.user;

import com.tungstun.common.response.UuidResponse;
import com.tungstun.security.application.user.UserCommandHandler;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.application.user.command.RegisterUser;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.web.user.converter.UserConverter;
import com.tungstun.security.port.web.user.request.UserRegistrationRequest;
import com.tungstun.security.port.web.user.response.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserQueryHandler userQueryHandler;
    private final UserCommandHandler userCommandHandler;
    private final UserConverter converter;

    public AccountController(UserQueryHandler userQueryHandler, UserCommandHandler userCommandHandler, UserConverter converter) {
        this.userQueryHandler = userQueryHandler;
        this.userCommandHandler = userCommandHandler;
        this.converter = converter;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registers a user",
            description = "Create a new user account given the provided information"
    )
    public UuidResponse register(
            @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
    ) throws AccountException {
        RegisterUser command = new RegisterUser(
                userRegistrationRequest.username(),
                userRegistrationRequest.password(),
                userRegistrationRequest.firstName(),
                userRegistrationRequest.lastName(),
                userRegistrationRequest.mail(),
                userRegistrationRequest.phoneNumber()
        );
        return new UuidResponse(userCommandHandler.registerUser(command));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Gets account information",
            description = "Gets all account information of the logged in in user"
    )
    public AccountResponse getAccountInformation(@Parameter(hidden = true) Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userQueryHandler.loadUserByUsername(userDetails.getUsername());

        return converter.convert(user);
    }
}
