package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderLineRepository;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.domain.OrderLine;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderLineService {
    private SpringOrderLineRepository SPRING_ORDER_LINE_REPOSITORY;
    private OrderService ORDER_SERVICE;

    public OrderLineService(SpringOrderLineRepository springOrderLineRepository, OrderService orderService) {
        this.SPRING_ORDER_LINE_REPOSITORY = springOrderLineRepository;
        this.ORDER_SERVICE = orderService;
    }

    private List<OrderLine> extractOrderLinesFromOrders(List<Order> orders) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (Order order : orders) {
            orderLines.addAll(order.getOrderLines());
        }
        return orderLines;
    }

    private List<OrderLine> extractOrderLinesFromOrderWithId(List<Order> orders, Long orderId) throws NotFoundException {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order.getOrderLines();
            }
        }
        throw new NotFoundException(String.format("No order lines found in order with id %s", orderId));
    }


    public List<OrderLine> getAllOrderLinesOfBar(Long barId) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBar(barId);
        List<OrderLine> orderLines = extractOrderLinesFromOrders(orders);
        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found for bar with id: %s", barId));
        return orderLines;
    }

    public List<OrderLine> getAllOrderLinesOfSession(Long barId, Long sessionId) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
        List<OrderLine> orderLines = extractOrderLinesFromOrders(orders);
        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found in session with id: %s", sessionId));
        return orderLines;
    }

    public List<OrderLine> getAllOrderLinesOfOrder(Long barId, Long sessionId, Long orderId) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
        List<OrderLine> orderLines = extractOrderLinesFromOrderWithId(orders, orderId);
        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found in session with id: %s", sessionId));
        return orderLines;
    }

    public OrderLine getOrderLineOfOrder(Long barId, Long sessionId, Long orderId, Long orderLineId) throws NotFoundException {
        Order order = this.ORDER_SERVICE.getOrderOfSession(barId, sessionId, orderId);
        for (OrderLine orderLine : order.getOrderLines()) {
            if (orderLine.getId().equals(orderLineId)) {
                return orderLine;
            }
        }
        throw new NotFoundException(String.format("No order line found in order with id: %s", orderId));
    }
}
