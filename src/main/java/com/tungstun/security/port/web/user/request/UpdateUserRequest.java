package com.tungstun.security.port.web.user.request;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber) {
}
