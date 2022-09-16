package com.tungstun.barapi.application.bill.command;

import java.util.UUID;

public record AddCustomerToSession(
        UUID barId,
        UUID sessionId,
        UUID customerId) {
}
