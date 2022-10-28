package com.tungstun.barapi.port.web.person.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonResponse(
        UUID id,
        String name,
        UUID userId) {
}
