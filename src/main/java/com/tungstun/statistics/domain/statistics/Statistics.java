package com.tungstun.statistics.domain.statistics;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;

public record Statistics(
        OrderProduct mostSoldProduct,
        Bill mostExpensiveBill,
        double totalSpent,
        double totalNotYetPayed) {
}
