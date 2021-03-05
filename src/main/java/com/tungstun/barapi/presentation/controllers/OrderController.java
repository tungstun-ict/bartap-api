package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.order.Order;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/")
@RolesAllowed("ROLE_BAR_OWNER")
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

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    public ResponseEntity<List<OrderResponse>> getAllSessionOrders(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBill(barId, sessionId, billId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFromSession(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId,
            @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        Order order = this.ORDER_SERVICE.getOrderOfBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(convertToOrderResult(order), HttpStatus.OK);
    }

    @DeleteMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId,
            @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        this.ORDER_SERVICE.deleteOrderFromBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    public ResponseEntity<OrderResponse> addProductToOrder(
            @PathVariable("barId") Long barId,
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("billId") Long billId,
            @Valid @RequestBody OrderRequest orderLineRequest
    ) throws NotFoundException {
        Order order = this.ORDER_SERVICE.addProductToBill(barId, sessionId, billId, orderLineRequest);
        return new ResponseEntity<>(convertToOrderResult(order), HttpStatus.OK);
    }
}
