package com.tungstun.barapi.application.bill.query;

import java.util.UUID;

public record GetActiveBillOfUser(
        UUID barId,
        UUID userId) {
}
