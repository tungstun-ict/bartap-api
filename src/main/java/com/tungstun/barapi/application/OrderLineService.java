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

    public List<OrderLine> getAllOrderLinesOfBar(Long barId) throws NotFoundException {
        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBar(barId);
        List<OrderLine> orderLines = new ArrayList<>();
        for (Order order : orders) {
            orderLines.addAll(order.getOrderLines());
        }
        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found for bar with id: %s", barId));
        return orderLines;
    }
}
