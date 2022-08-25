package com.tungstun.security.application.user.command;

public record UpdateUser(
        String username,
        String firstName,
        String lastName,
        String phoneNumber) {
}
