package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record ListOrderHistory(
        UUID barId,
        UUID sessionId,
        UUID billId) {
}
