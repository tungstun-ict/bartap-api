package com.tungstun.barapi.port.web.order.converter;

import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.port.web.order.response.OrderResponse;
import com.tungstun.barapi.port.web.person.converter.PersonConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    private final PersonConverter personConverter;
    private final OrderProductConverter orderProductConverter;

    public OrderConverter(PersonConverter personConverter, OrderProductConverter orderProductConverter) {
        this.personConverter = personConverter;
        this.orderProductConverter = orderProductConverter;
    }

    public OrderResponse convert(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getAmount(),
                order.getCreationDate(),
                orderProductConverter.convert(order.getProduct()),
                personConverter.convert(order.getBartender())
        );
    }
    public List<OrderResponse> convertAll(List<Order> orders) {
        return orders.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
