package com.tungstun.barapi.presentation.dto.converter;

import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    private final PersonConverter personConverter;

    public OrderConverter(PersonConverter personConverter) {
        this.personConverter = personConverter;
    }

    public OrderResponse convert(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setAmount(order.getAmount());
        response.setCreationDate(order.getCreationDate());
        response.setCustomer(personConverter.convert(order.getBill().getCustomer()));
        response.setBartender(personConverter.convert(order.getBartender()));
        response.setProduct(order.getProduct());
        return response;
    }
    public List<OrderResponse> convertAll(List<Order> orders) {
        return orders.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
