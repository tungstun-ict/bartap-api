package com.tungstun.security.port.web.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Map<UUID, String> connectedBars) {
}
