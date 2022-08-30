package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record ListOrderHistoryOfSession(
        UUID barId,
        UUID sessionId) {
}
