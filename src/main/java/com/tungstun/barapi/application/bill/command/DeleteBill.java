package com.tungstun.barapi.application.bill.command;

import java.util.UUID;

public record DeleteBill(
        UUID barId,
        UUID sessionId,
        UUID billId) {
}
