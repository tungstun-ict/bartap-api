package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.presentation.dto.converter.OrderConverter;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/bars/{barId}/")
public class OrderController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;

    public OrderController(OrderService orderService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @GetMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all orders of bar",
            notes = "Provide id of bar to look up all orders that are linked to the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public List<OrderResponse> getAllBarOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfBar(barId);
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all orders of session of bar",
            notes = "Provide id of bar and session to look up all orders that are linked session of the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public List<OrderResponse> getAllSessionOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfSession(barId, sessionId);
        return orderConverter.convertAll(orders);
    }

//    @GetMapping("sessions/{sessionId}/orders/{orderId}")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
//    @ApiOperation(
//            value = "Finds order of session of bar",
//            notes = "Provide id of bar, session and order to look up specific order of session of the bar",
//            response = OrderResponse.class
//    )
//    public OrderResponse getOrderFromSession(
//            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
//            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
//            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") UUID orderId
//    ) throws EntityNotFoundException {
//        Order order = this.orderService.getOrderOfSession(barId, sessionId, orderId);
//        return orderConverter.convert(order);
//    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds orders of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up orders of bill of session of the bar",
            response = OrderResponse.class
    )
    public List<OrderResponse> getAllBillOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve orders from") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        List<Order> orders = this.orderService.getAllOrdersOfBill(barId, sessionId, billId);
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up specific order of bill of session of the bar",
            response = OrderResponse.class
    )
    public OrderResponse getOrderFromBill(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve the order from") @PathVariable("billId") UUID billId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        Order order = this.orderService.getOrderOfBill(barId, sessionId, billId, orderId);
        return orderConverter.convert(order);
    }

    @DeleteMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to delete specific order of bill of session of the bar"
    )
    public void deleteOrder(
            @ApiParam(value = "ID value for the bar you want to delete the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to delete the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to delete the order from") @PathVariable("billId") UUID billId,
            @ApiParam(value = "ID value for the order you want to delete") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        this.orderService.deleteOrderFromBill(barId, sessionId, billId, orderId);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Create new order for bill of session of bar",
            notes = "Provide id of bar, session and bill to create a new order with information from request body",
            response = OrderResponse.class
    )
    public UUID createNewOrder(
            @ApiParam(value = "ID value for the bar you want to add the new order to") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to add the new order to") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to add the new order to") @PathVariable("billId") UUID billId,
            @Valid @RequestBody OrderRequest orderLineRequest,
            @ApiIgnore Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return orderService.addProductToBill(barId, sessionId, billId, orderLineRequest, userDetails.getUsername());
    }
}
