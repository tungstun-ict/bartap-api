package com.tungstun.security.presentation.dto.converter;

import com.tungstun.security.domain.user.User;
import com.tungstun.security.presentation.dto.response.AccountResponse;
import com.tungstun.security.presentation.dto.response.UserAccountSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
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
        response.setConnectedBars(getBarIds(user));
        return response;
    }

    private Set<Long> getBarIds(User user) {
        return user.getAuthorizations().keySet();
    }

    public List<AccountResponse> convertAll(List<User> users) {
        return users.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
