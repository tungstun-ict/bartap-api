package com.tungstun.barapi.application.statistics.query;

import java.util.UUID;

public record GetCustomerStatistics(
        UUID barId,
        UUID userId) {
}
