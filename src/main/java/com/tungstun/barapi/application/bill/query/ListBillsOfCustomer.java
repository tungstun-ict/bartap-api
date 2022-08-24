package com.tungstun.barapi.application.bill.query;

import java.util.UUID;

public record ListBillsOfCustomer(
        UUID barId,
        UUID customerId) {
}
