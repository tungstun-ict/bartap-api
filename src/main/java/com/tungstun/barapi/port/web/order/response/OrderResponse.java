package com.tungstun.barapi.port.web.order.response;

import com.tungstun.barapi.port.web.person.response.PersonResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        int amount,
        LocalDateTime creationDate,
        OrderProductResponse product,
        PersonResponse bartender) {
}
