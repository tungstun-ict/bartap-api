package com.tungstun.statistics.application.statistics.query;

import java.util.UUID;

public record GetUserCustomerStatistics(
        UUID barId,
        UUID userId) {
}
