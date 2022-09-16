package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record GetOrder(
        UUID barId,
        UUID sessionId,
        UUID billId,
        UUID orderId){
}
