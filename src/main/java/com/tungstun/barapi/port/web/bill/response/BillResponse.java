package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.port.web.order.response.OrderResponse;
import com.tungstun.barapi.port.web.person.response.PersonResponse;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID id,
        boolean isPayed,
        PersonResponse customer,
        Double totalPrice,
        List<OrderResponse> orders) {
}
