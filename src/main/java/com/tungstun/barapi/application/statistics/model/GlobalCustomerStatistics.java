package com.tungstun.barapi.application.statistics.model;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;

public record GlobalCustomerStatistics(
        Double totalSpent,
        Double totalNotYetPayed,
        Bill highestPricedBill,
        OrderProduct favoriteProduct) {
}
