package com.tungstun.security.presentation.converter;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.presentation.dto.response.AccountResponse;
import com.tungstun.security.presentation.dto.response.UserAccountSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public UserAccountSummaryResponse convertToSummary(User user) {
        UserAccountSummaryResponse response = new UserAccountSummaryResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMail(user.getMail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }

    public List<UserAccountSummaryResponse> convertAllSummary(List<User> users) {
        return users.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }

    public AccountResponse convert(User user) {
        AccountResponse response = new AccountResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getMail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setConnectedBars(this.getBarIds(user));
        return response;
    }

    private List<Long> getBarIds(User user) {
        return user.getUserBarAuthorizations().stream()
                .map(UserBarAuthorization::getBar)
                .map(Bar::getId)
                .collect(Collectors.toList());
    }

    public List<AccountResponse> convertAll(List<User> users) {
        return users.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
