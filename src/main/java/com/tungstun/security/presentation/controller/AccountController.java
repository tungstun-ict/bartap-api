package com.tungstun.security.presentation.controller;

import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
import com.tungstun.security.presentation.converter.UserConverter;
import com.tungstun.security.presentation.dto.response.AccountResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserService userService;
    private final UserConverter converter;

    public AccountController(UserService userService, UserConverter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    @GetMapping
    @ApiOperation(
            value = "Gets all information of logged in user"
    )
    public ResponseEntity<AccountResponse> getAccountInformation(@ApiIgnore Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var user = (User) userService.loadUserByUsername(userDetails.getUsername());

        return new ResponseEntity<>(converter.convert(user), HttpStatus.OK);
    }
}
