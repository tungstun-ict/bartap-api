package com.tungstun.security.port.web.user.response;

import java.util.UUID;

public record UserAccountSummaryResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String mail,
        String phoneNumber) {
}
