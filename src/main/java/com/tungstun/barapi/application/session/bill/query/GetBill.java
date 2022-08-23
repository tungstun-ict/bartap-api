package com.tungstun.barapi.application.session.bill.query;

import java.util.UUID;

public record GetBill(
        UUID billId,
        Long sessionId,
        Long barId) {
}
