package com.tungstun.barapi.application.statistics.model;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.OrderProduct;

public record BarStatistics(
        OrderProduct mostSoldProductOfLastMonth ,
        OrderProduct mostSoldProductOfLastYear,
        OrderProduct mostSoldProductOfAllTime,

        Bill mostExpensiveBillOfLastMonth,
        Bill mostExpensiveBillOfLastYear,
        Bill mostExpensiveBillOfAllTime,

        double totalSpentLastMonth,
        double totalSpentOfLastYear,
        double totalSpentOfAllTime,

        double totalNotYetPayedLastMonth,
        double totalNotYetPayedLastYear,
        double totalNotYetPayedAllTime) {
}
