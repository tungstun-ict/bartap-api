package com.tungstun.barapi.port.web.category.response;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name) {
}
