package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record ListOrdersOfBill(
        UUID barId,
        UUID sessionId,
        UUID billId){
}
