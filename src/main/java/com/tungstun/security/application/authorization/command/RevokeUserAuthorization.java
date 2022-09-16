package com.tungstun.security.application.authorization.command;

import java.util.UUID;

public record RevokeUserAuthorization(
        Long ownerId,
        UUID barId,
        Long userId) {
}
