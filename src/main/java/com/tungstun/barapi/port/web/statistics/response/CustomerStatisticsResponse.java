package com.tungstun.barapi.port.web.statistics.response;

import com.tungstun.barapi.port.web.bill.response.BillResponse;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;

public record CustomerStatisticsResponse(
        Double totalSpent,
        Double totalNotYetPayed,
        BillResponse highestPricedBill,
        OrderProductResponse favoriteProduct) {
}
