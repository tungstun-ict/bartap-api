package com.tungstun.barapi.port.web.order;

import com.tungstun.barapi.application.order.OrderCommandHandler;
import com.tungstun.barapi.application.order.OrderQueryHandler;
import com.tungstun.barapi.application.order.command.AddOrder;
import com.tungstun.barapi.application.order.command.RemoveOrder;
import com.tungstun.barapi.application.order.query.*;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.bill.OrderHistoryEntry;
import com.tungstun.barapi.port.web.order.converter.OrderConverter;
import com.tungstun.barapi.port.web.order.converter.OrderHistoryEntryConverter;
import com.tungstun.barapi.port.web.order.request.CreateOrderRequest;
import com.tungstun.barapi.port.web.order.response.OrderHistoryEntryResponse;
import com.tungstun.barapi.port.web.order.response.OrderResponse;
import com.tungstun.common.response.UuidResponse;
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
    private final OrderQueryHandler orderQueryHandler;
    private final OrderCommandHandler orderCommandHandler;
    private final OrderConverter orderConverter;
    private final OrderHistoryEntryConverter orderHistoryEntryConverter;

    public OrderController(OrderQueryHandler orderQueryHandler, OrderCommandHandler orderCommandHandler, OrderConverter orderConverter, OrderHistoryEntryConverter orderHistoryEntryConverter) {
        this.orderQueryHandler = orderQueryHandler;
        this.orderCommandHandler = orderCommandHandler;
        this.orderConverter = orderConverter;
        this.orderHistoryEntryConverter = orderHistoryEntryConverter;
    }

    @GetMapping("sessions/{sessionId}/order-history")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds the full order history of session of bar",
            notes = "Provide id of bar and session to look up full order history that are linked session",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public List<OrderHistoryEntryResponse> getSessionOrderHistory(
            @ApiParam(value = "ID value for the bar you want to retrieve order history from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve order history from") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        List<OrderHistoryEntry> orderHistory = orderQueryHandler.handle(new ListOrderHistoryOfSession(barId, sessionId));
        return orderHistoryEntryConverter.convertAll(orderHistory);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/order-history")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds the full order history of bill of session",
            notes = "Provide id of bar, session and bill to look up full order history that are linked bill",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public List<OrderHistoryEntryResponse> getBillOrderHistory(
            @ApiParam(value = "ID value for the bar you want to retrieve order history from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve order history from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve order history from") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        List<OrderHistoryEntry> orderHistory = orderQueryHandler.handle(new ListOrderHistory(barId, sessionId, billId));
        return orderHistoryEntryConverter.convertAll(orderHistory);
    }

    @GetMapping("sessions/{sessionId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds all orders of session of bar",
            notes = "Provide categoryId of bar and session to look up all orders that are linked session of the bar",
            response = OrderResponse.class,
            responseContainer = "List"
    )
    public List<OrderResponse> getAllSessionOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        List<Order> orders = orderQueryHandler.handle(new ListOrdersOfSession(barId, sessionId));
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds orders of bill of session of bar",
            notes = "Provide categoryId of bar, session, bill and order to look up orders of bill of session of the bar",
            response = OrderResponse.class
    )
    public List<OrderResponse> getAllBillOrders(
            @ApiParam(value = "ID value for the bar you want to retrieve orders from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve orders from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve orders from") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        List<Order> orders = orderQueryHandler.handle(new ListOrdersOfBill(barId, sessionId, billId));
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Finds order of bill of session of bar",
            notes = "Provide categoryId of bar, session, bill and order to look up specific order of bill of session of the bar",
            response = OrderResponse.class
    )
    public OrderResponse getOrderFromBill(
            @ApiParam(value = "ID value for the bar you want to retrieve the order from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to retrieve the order from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to retrieve the order from") @PathVariable("billId") UUID billId,
            @ApiParam(value = "ID value for the order you want to retrieve") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        Order order = orderQueryHandler.handle(new GetOrder(barId, sessionId, billId, orderId));
        return orderConverter.convert(order);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Create new order for bill of session of bar",
            notes = "Provide categoryId of bar, session and bill to create a new order with information from request body",
            response = OrderResponse.class
    )
    public UuidResponse createNewOrder(
            @ApiParam(value = "ID value for the bar you want to add the new order to") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to add the new order to") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to add the new order to") @PathVariable("billId") UUID billId,
            @Valid @RequestBody CreateOrderRequest request,
            @ApiIgnore Authentication authentication
    ) throws EntityNotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AddOrder command = new AddOrder(
                barId,
                sessionId,
                billId,
                request.productId(),
                request.amount(),
                userDetails.getUsername()
        );
        return new UuidResponse(orderCommandHandler.handle(command));
    }

    @DeleteMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @ApiOperation(
            value = "Deletes order of bill of session of bar",
            notes = "Provide categoryId of bar, session, bill and order to delete specific order of bill of session of the bar"
    )
    public void deleteOrder(
            @ApiParam(value = "ID value for the bar you want to delete the order from") @PathVariable("barId") UUID barId,
            @ApiParam(value = "ID value for the session you want to delete the order from") @PathVariable("sessionId") UUID sessionId,
            @ApiParam(value = "ID value for the bill you want to delete the order from") @PathVariable("billId") UUID billId,
            @ApiParam(value = "ID value for the order you want to delete") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        RemoveOrder command = new RemoveOrder(barId, sessionId, billId, orderId);
        orderCommandHandler.handle(command);
    }
}
