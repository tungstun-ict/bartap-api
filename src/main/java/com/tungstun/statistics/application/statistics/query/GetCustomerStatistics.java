package com.tungstun.statistics.application.statistics.query;

import java.util.UUID;

public record GetCustomerStatistics(
        UUID barId,
        UUID customerId) {
}
