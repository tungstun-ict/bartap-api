package com.tungstun.security.application.user.query;

import java.util.UUID;

public record GetUserSummary(
        UUID userId,
        String ownerUsername) {
}
