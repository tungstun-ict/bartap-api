package com.tungstun.barapi.application.bill.query;

import java.util.UUID;

public record GetBillAsCustomer(
        UUID barId,
        UUID sessionId,
        UUID billId,
        UUID userId) {
}
