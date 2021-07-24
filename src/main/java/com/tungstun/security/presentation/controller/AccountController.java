package com.tungstun.security.presentation.controller;

import com.tungstun.barapi.application.BarService;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.security.data.model.User;
import com.tungstun.security.presentation.dto.response.AccountResponse;
import com.tungstun.security.application.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserService USER_SERVICE;
    private final BarService BAR_SERVICE;

    public AccountController(UserService USER_SERVICE, BarService BAR_SERVICE) {
        this.USER_SERVICE = USER_SERVICE;
        this.BAR_SERVICE = BAR_SERVICE;
    }

    @GetMapping
    @ApiOperation(
            value = "Gets all information of logged in user"
    )
    public ResponseEntity<AccountResponse> getAccountInformation(@ApiIgnore Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var user = (User) USER_SERVICE.loadUserByUsername(userDetails.getUsername());

        return new ResponseEntity<>(convertUserToAccountResponse(user), HttpStatus.OK);
    }

    private AccountResponse convertUserToAccountResponse(User user) {
        var bars = BAR_SERVICE.getAllBarOwnerBars(user.getUsername());
        var barIds = bars.stream().map(Bar::getId);

        return new AccountResponse(user.getId(), user.getMail(), user.getUsername(), user.getFirstName(), user.getLastName(),
                barIds.collect(Collectors.toList()));
    }
}
