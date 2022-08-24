package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record GetOrder(
        UUID orderId,
        UUID billId,
        UUID sessionId,
        UUID barId){
}
