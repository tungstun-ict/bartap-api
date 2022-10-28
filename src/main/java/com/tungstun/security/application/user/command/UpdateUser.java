package com.tungstun.security.application.user.command;

import java.util.UUID;

public record UpdateUser(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber) {
}
