package com.tungstun.barapi.application.bill.command;

import java.util.UUID;

public record PayBill(
        UUID barId,
        UUID sessionId,
        UUID billId) {
}
