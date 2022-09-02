package com.tungstun.barapi.port.web.order.response;

import com.tungstun.barapi.port.web.person.response.PersonResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderHistoryEntryResponse(
        UUID id,
        String type,
        LocalDateTime date,
        UUID productId,
        String productName,
        Integer amount,
        PersonResponse customer,
        PersonResponse bartender) {
}
