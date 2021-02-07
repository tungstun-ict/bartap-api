package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("bars/{barId}/")
public class OrderController {
    private final OrderService ORDER_SERVICE;
    private final ResponseMapper RESPONSE_MAPPER;

    public OrderController(OrderService orderService, ResponseMapper responseMapper) {
        this.ORDER_SERVICE = orderService;
        this.RESPONSE_MAPPER = responseMapper;
    }

    private OrderResponse convertToOrderResult(Order order){
        return RESPONSE_MAPPER.convert(order, OrderResponse.class);
    }
}
