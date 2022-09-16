package com.tungstun.statistics.domain.statistics.model;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;

public record BarStatistics(
        OrderProduct mostSoldProduct,
        Bill mostExpensiveBill,
        double totalSpent,
        double totalNotYetPayed) {
}
