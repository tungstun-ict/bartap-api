package com.tungstun.barapi.application.bill.query;

import java.util.UUID;

public record GetBill(
        UUID barId,
        UUID sessionId,
        UUID billId) {
}
