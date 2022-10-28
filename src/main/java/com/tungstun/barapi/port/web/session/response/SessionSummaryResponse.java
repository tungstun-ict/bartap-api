package com.tungstun.barapi.port.web.session.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionSummaryResponse(
        UUID id,
        String name,
        LocalDateTime date) {
}
