package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;

public record CustomerStatistics(
        Double totalSpent,
        Double totalNotYetPayed,
        Bill highestPricedBill,
        OrderProduct favoriteProduct) {
}
