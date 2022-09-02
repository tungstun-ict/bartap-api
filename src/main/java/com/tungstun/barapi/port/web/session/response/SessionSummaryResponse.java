package com.tungstun.barapi.port.web.session.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionSummaryResponse(
        UUID id,
        String name) {
}
