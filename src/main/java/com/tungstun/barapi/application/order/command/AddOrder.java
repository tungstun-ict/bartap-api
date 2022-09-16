package com.tungstun.barapi.application.order.command;

import java.util.UUID;

public record AddOrder(
        UUID barId,
        UUID sessionId,
        UUID billId,
        UUID productId,
        Integer amount,
        String bartenderUsername) {
}
