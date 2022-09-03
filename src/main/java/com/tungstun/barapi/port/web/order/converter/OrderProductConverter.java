package com.tungstun.barapi.port.web.order.converter;

import com.tungstun.barapi.domain.bill.OrderProduct;
import com.tungstun.barapi.port.web.order.response.OrderProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderProductConverter {
    public OrderProductResponse convert(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getBrand(),
                orderProduct.getPrice().amount().doubleValue()
        );
    }
    public List<OrderProductResponse> convertAll(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
