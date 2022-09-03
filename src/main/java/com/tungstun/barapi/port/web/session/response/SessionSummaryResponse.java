package com.tungstun.barapi.port.web.session.response;

import java.util.UUID;

public record SessionSummaryResponse(
        UUID id,
        String name) {
}
