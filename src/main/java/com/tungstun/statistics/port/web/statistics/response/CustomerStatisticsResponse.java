package com.tungstun.statistics.port.web.statistics.response;

import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;

public record CustomerStatisticsResponse(
        Double totalSpent,
        Double totalNotYetPayed,
        BillSummaryResponse highestPricedBill,
        OrderProductResponse favoriteProduct) {
}
