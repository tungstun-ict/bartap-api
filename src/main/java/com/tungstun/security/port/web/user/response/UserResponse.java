package com.tungstun.security.port.web.user.response;

import java.util.Map;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Map<UUID, String> connectedBars) {
}
