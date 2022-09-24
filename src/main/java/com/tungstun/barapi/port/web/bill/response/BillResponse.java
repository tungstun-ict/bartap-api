package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.port.web.order.response.OrderResponse;
import com.tungstun.barapi.port.web.person.response.PersonResponse;
import com.tungstun.barapi.port.web.session.response.SessionSummaryResponse;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID id,
        Boolean isPayed,
        PersonResponse customer,
        Double totalPrice,
        List<OrderResponse> orders,
        SessionSummaryResponse session) {
}
