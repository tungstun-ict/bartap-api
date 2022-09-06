package com.tungstun.barapi.port.web.statistics.response;

import com.tungstun.barapi.port.web.bill.response.BillSummaryResponse;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;

public record GlobalCustomerStatisticsResponse(
        Double totalSpent,
        Double totalNotYetPayed,
        BillSummaryResponse highestPricedBill,
        OrderProductResponse favoriteProduct) {
}
