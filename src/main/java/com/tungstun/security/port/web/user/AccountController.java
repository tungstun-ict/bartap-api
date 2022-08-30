package com.tungstun.security.port.web.user;

import com.tungstun.common.response.IdResponse;
import com.tungstun.security.application.user.UserCommandHandler;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.application.user.command.RegisterUser;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.web.user.converter.UserConverter;
import com.tungstun.security.port.web.user.request.UserRegistrationRequest;
import com.tungstun.security.port.web.user.response.AccountResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
            value = "Registers new user",
            notes = "Provide account information in the request body to create a new user account"
    )
    public IdResponse register(
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
        return new IdResponse(userCommandHandler.registerUser(command));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets all information of logged in user")
    public AccountResponse getAccountInformation(@ApiIgnore Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userQueryHandler.loadUserByUsername(userDetails.getUsername());

        return converter.convert(user);
    }
}
