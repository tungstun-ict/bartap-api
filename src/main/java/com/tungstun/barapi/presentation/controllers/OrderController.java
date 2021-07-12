package com.tungstun.barapi.presentation.controllers;

import com.tungstun.barapi.application.OrderService;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.barapi.presentation.dto.response.BillResponse;
import com.tungstun.barapi.presentation.dto.response.OrderResponse;
import com.tungstun.barapi.presentation.mapper.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/bars/{barId}/")
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
    private BillResponse convertToBillResult(Bill bill){
        return RESPONSE_MAPPER.convert(bill, BillResponse.class);
    }

    @GetMapping("orders")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all orders of bar",
            notes = "Provide id of bar to look up all orders that are linked to the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<OrderResponse>> getAllBarOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBar(barId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds all orders of session of bar",
            notes = "Provide id of bar and session to look up all orders that are linked session of the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<OrderResponse>> getAllSessionOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds order of session of bar",
            notes = "Provide id of bar, session and order to look up specific order of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<OrderResponse> getOrderFromSession(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        Order order = this.ORDER_SERVICE.getOrderOfSession(barId, sessionId, orderId);
        return new ResponseEntity<>(convertToOrderResult(order), HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds orders of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up orders of bill of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<List<OrderResponse>> getAllSessionOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve orders from") @PathVariable("billId") Long billId
    ) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBill(barId, sessionId, billId);
        List<OrderResponse> orderResponses = RESPONSE_MAPPER.convertList(orders, OrderResponse.class);
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Finds order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to look up specific order of bill of session of the bar",
            response = OrderResponse.class
    )
    public ResponseEntity<OrderResponse> getOrderFromSession(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve the order from") @PathVariable("billId") Long billId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        Order order = this.ORDER_SERVICE.getOrderOfBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(convertToOrderResult(order), HttpStatus.OK);
    }

    @DeleteMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Deletes order of bill of session of bar",
            notes = "Provide id of bar, session, bill and order to delete specific order of bill of session of the bar"
    )
    public ResponseEntity<Void> deleteOrder(
            @ApiParam(value = "ID value for the bar you want to delete the order from") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to delete the order from") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to delete the order from") @PathVariable("billId") Long billId,
            @ApiParam(value = "ID value for the order you want to delete") @PathVariable("orderId") Long orderId
    ) throws NotFoundException {
        this.ORDER_SERVICE.deleteOrderFromBill(barId, sessionId, billId, orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    @PreAuthorize("hasPermission(#barId, 'ROLE_BAR_OWNER')")
    @ApiOperation(
            value = "Create new order for bill of session of bar",
            notes = "Provide id of bar, session and bill to create a new order with information from request body",
            response = OrderResponse.class
    )
    public ResponseEntity<BillResponse> createNewOrder(
            @ApiParam(value = "ID value for the bar you want to add the new order to") @PathVariable("barId") Long barId,
            @ApiParam(value = "ID value for the session you want to add the new order to") @PathVariable("sessionId") Long sessionId,
            @ApiParam(value = "ID value for the bill you want to add the new order to") @PathVariable("billId") Long billId,
            @Valid @RequestBody OrderRequest orderLineRequest,
            @ApiIgnore Authentication authentication
    ) throws NotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Bill bill = this.ORDER_SERVICE.addProductToBill(barId, sessionId, billId, orderLineRequest, userDetails.getUsername());
        return new ResponseEntity<>(convertToBillResult(bill), HttpStatus.OK);
    }
}
