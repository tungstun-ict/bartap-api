package com.tungstun.barapi.port.web.statistics.response;

import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;

public record BarStatisticsResponse(
        OrderProductResponse mostSoldProductOfLastMonth ,
        OrderProductResponse mostSoldProductOfLastYear,
        OrderProductResponse mostSoldProductOfAllTime,

        BillSummaryResponse mostExpensiveBillOfLastMonth,
        BillSummaryResponse mostExpensiveBillOfLastYear,
        BillSummaryResponse mostExpensiveBillOfAllTime,

        double totalSpentLastMonth,
        double totalSpentOfLastYear,
        double totalSpentOfAllTime,

        double totalNotYetPayedLastMonth,
        double totalNotYetPayedLastYear,
        double totalNotYetPayedAllTime) {
}
