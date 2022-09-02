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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Operation(
            summary = "Finds the order history of a session",
            description = "Find the full order history of all orders added and removed during the session",
            tags = "Order"
    )
    public List<OrderHistoryEntryResponse> getSessionOrderHistory(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        List<OrderHistoryEntry> orderHistory = orderQueryHandler.handle(new ListOrderHistoryOfSession(barId, sessionId));
        return orderHistoryEntryConverter.convertAll(orderHistory);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/order-history")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds the order history of a bill",
            description = "Find the full order history of all orders added to and removed from a bill during its session",
            tags = "Order"
    )
    public List<OrderHistoryEntryResponse> getBillOrderHistory(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        List<OrderHistoryEntry> orderHistory = orderQueryHandler.handle(new ListOrderHistory(barId, sessionId, billId));
        return orderHistoryEntryConverter.convertAll(orderHistory);
    }

    @GetMapping("sessions/{sessionId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds orders of a session",
            description = "Find all orders of a session of a bar with the given id's",
            tags = "Order"
    )
    public List<OrderResponse> getAllSessionOrders(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId
    ) throws EntityNotFoundException {
        List<Order> orders = orderQueryHandler.handle(new ListOrdersOfSession(barId, sessionId));
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds orders of bill",
            description = "Finds all orders of a bill of a session of a bar with the given id's",
            tags = "Order"
    )
    public List<OrderResponse> getAllBillOrders(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id value of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId
    ) throws EntityNotFoundException {
        List<Order> orders = orderQueryHandler.handle(new ListOrdersOfBill(barId, sessionId, billId));
        return orderConverter.convertAll(orders);
    }

    @GetMapping("sessions/{sessionId}/bills/{billId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Finds an order",
            description = "Find an order of a bill of a session of a bar with the given id's",
            tags = "Order"
    )
    public OrderResponse getOrderFromBill(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id values of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId,
            @Parameter(description = "Id value of the order") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        Order order = orderQueryHandler.handle(new GetOrder(barId, sessionId, billId, orderId));
        return orderConverter.convert(order);
    }

    @PutMapping("sessions/{sessionId}/bills/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#barId, {'OWNER','BARTENDER'})")
    @Operation(
            summary = "Adds new order to bill",
            description = "Create a new order and add it the the bill of a session of a bar with the given id's",
            tags = "Order"
    )
    public UuidResponse createNewOrder(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id values of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId,
            @Valid @RequestBody CreateOrderRequest request,
            @Parameter(hidden = true) Authentication authentication
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
    @Operation(
            summary = "Remove order from bill",
            description = "Remove an order from a bill of a session of a bar",
            tags = "Order"
    )
    public void deleteOrder(
            @Parameter(description = "Id value of the bar") @PathVariable("barId") UUID barId,
            @Parameter(description = "Id values of the session") @PathVariable("sessionId") UUID sessionId,
            @Parameter(description = "Id value of the bill") @PathVariable("billId") UUID billId,
            @Parameter(description = "Id value of the order") @PathVariable("orderId") UUID orderId
    ) throws EntityNotFoundException {
        RemoveOrder command = new RemoveOrder(barId, sessionId, billId, orderId);
        orderCommandHandler.handle(command);
    }
}
