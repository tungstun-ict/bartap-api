package com.tungstun.statistics.port.web.statistics.response;

import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;

public record BarStatisticsResponse(
        OrderProductResponse mostSoldProduct,
        BillSummaryResponse mostExpensiveBill,
        double totalSpent,
        double totalNotYetPayed) {
}
