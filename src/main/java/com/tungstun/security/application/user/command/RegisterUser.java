package com.tungstun.security.application.user.command;

public record RegisterUser(
        String username,
        String password,
        String firstName,
        String lastName,
        String mail,
        String phoneNumber) {
}
