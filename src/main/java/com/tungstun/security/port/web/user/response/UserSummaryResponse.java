package com.tungstun.security.port.web.user.response;

import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String username,
        String firstName,
        String lastName) {
}
