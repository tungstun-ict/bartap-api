package com.tungstun.security.application.authorization.command;

import java.util.UUID;

public record AuthorizeNewBarOwnership(
        Long userId,
        UUID barId) {
}
