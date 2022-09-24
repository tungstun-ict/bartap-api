package com.tungstun.barapi.application.bill.query;

import java.util.UUID;

public record ListBillsOfUser(
        UUID barId,
        String username) {
}
