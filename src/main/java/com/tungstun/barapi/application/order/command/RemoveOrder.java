package com.tungstun.barapi.application.order.command;

import java.util.UUID;

public record RemoveOrder(
        UUID barId,
        UUID sessionId,
        UUID billId,
        UUID orderId) {
}
