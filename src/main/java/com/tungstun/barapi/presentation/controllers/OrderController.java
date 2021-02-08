package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @GetMapping("orders")
    public ResponseEntity<List<OrderResponse>> getAllBarOrders(
            @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBar(barId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders")
    public ResponseEntity<List<OrderResponse>> getAllSessionOrders(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFromSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        Order order = this.ORDER_SERVICE.getOrderOfSession(barId, sessionId, orderId);
        return new ResponseEntity<>(convertToOrderResult(order), HttpStatus.OK);
    }
}
