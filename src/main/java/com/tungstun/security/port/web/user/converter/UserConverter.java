package com.tungstun.security.port.web.user.converter;

import com.tungstun.common.phonenumber.PhoneNumber;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.web.user.response.UserResponse;
import com.tungstun.security.port.web.user.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public UserResponse convert(User user) {
        String phoneNumber = Optional.ofNullable(user.getPhoneNumber())
                .map(PhoneNumber::getValue)
                .orElse(null);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getMail(),
                phoneNumber,
                user.getCreatedOn(),
                user.getAuthorizations()
        );
    }

    public List<UserResponse> convertAll(List<User> users) {
        return users.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public UserSummaryResponse convertToSummary(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public List<UserSummaryResponse> convertAllSummary(List<User> users) {
        return users.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
}
