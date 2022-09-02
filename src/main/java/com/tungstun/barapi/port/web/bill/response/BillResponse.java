package com.tungstun.barapi.port.web.bill.response;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.web.order.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID id,
        boolean isPayed,
        Person customer,
        Double totalPrice,
        List<OrderResponse> orders) {
}
