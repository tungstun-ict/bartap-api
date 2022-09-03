package com.tungstun.security.port.web.user.converter;

import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.web.user.response.AccountResponse;
import com.tungstun.security.port.web.user.response.UserAccountSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public AccountResponse convert(User user) {
        return new AccountResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getMail(),
                user.getPhoneNumber().getValue(),
                user.getAuthorizations()
        );
    }

    public List<AccountResponse> convertAll(List<User> users) {
        return users.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public UserAccountSummaryResponse convertToSummary(User user) {
        return new UserAccountSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getMail(),
                user.getPhoneNumber().getValue()
        );
    }

    public List<UserAccountSummaryResponse> convertAllSummary(List<User> users) {
        return users.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
}
