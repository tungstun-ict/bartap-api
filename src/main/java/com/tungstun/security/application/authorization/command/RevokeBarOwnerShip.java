package com.tungstun.security.application.authorization.command;

import java.util.UUID;

public record RevokeBarOwnerShip(
        Long ownerId,
        UUID barId) {
}
