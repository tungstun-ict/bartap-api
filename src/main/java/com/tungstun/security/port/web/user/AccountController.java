package com.tungstun.security.port.web.user;

import com.tungstun.common.response.UuidResponse;
import com.tungstun.security.application.user.UserCommandHandler;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.application.user.command.RegisterUser;
import com.tungstun.security.application.user.command.UpdateUser;
import com.tungstun.security.application.user.query.GetUserSummary;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.web.user.converter.UserConverter;
import com.tungstun.security.port.web.user.request.UpdateUserRequest;
import com.tungstun.security.port.web.user.request.UserRegistrationRequest;
import com.tungstun.security.port.web.user.response.UserResponse;
import com.tungstun.security.port.web.user.response.UserSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;
import java.util.UUID;

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
            description = "Create a new user account given the provided information",
            tags = "Account"
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

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update a user's account",
            description = "Update a user's first name, last name and phone number",
            tags = "Account"
    )
    public UuidResponse updateAccount(
            @PathVariable("userId") UUID id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ){
        UpdateUser command = new UpdateUser(
                id,
                updateUserRequest.firstName(),
                updateUserRequest.lastName(),
                updateUserRequest.phoneNumber()
        );
        return new UuidResponse(userCommandHandler.handle(command));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Gets account information",
            description = "Get all account information of the logged in in user",
            tags = "Account"
    )
    public UserResponse getAccountInformation(@Parameter(hidden = true) Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userQueryHandler.loadUserByUsername(userDetails.getUsername());

        return converter.convert(user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Gets account information",
            description = "Get all account information of the account with the given id",
            tags = "Account"
    )
    public UserSummaryResponse getAccountSummary(
            @PathVariable("userId") UUID id,
            @Parameter(hidden = true) Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userQueryHandler.handle(new GetUserSummary(id, userDetails.getUsername()));

        return converter.convertToSummary(user);
    }
}
