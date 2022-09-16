package com.tungstun.security.application.authorization.command;

import java.util.UUID;

public record AuthorizeUser(
        Long ownerId,
        String role,
        UUID barId,
        Long userId) {
}
