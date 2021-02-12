package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderLineRepository;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.domain.OrderLine;
import com.tungstun.barapi.domain.Product;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderLineService {
    private SpringOrderLineRepository SPRING_ORDER_LINE_REPOSITORY;

    public OrderLineService(SpringOrderLineRepository springOrderLineRepository) {
        this.SPRING_ORDER_LINE_REPOSITORY = springOrderLineRepository;
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

//
//    public List<OrderLine> getAllOrderLinesOfBar(Long barId) throws NotFoundException {
//        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfBar(barId);
//        List<OrderLine> orderLines = extractOrderLinesFromOrders(orders);
//        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found for bar with id: %s", barId));
//        return orderLines;
//    }
//
//    public List<OrderLine> getAllOrderLinesOfSession(Long barId, Long sessionId) throws NotFoundException {
//        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
//        List<OrderLine> orderLines = extractOrderLinesFromOrders(orders);
//        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found in session with id: %s", sessionId));
//        return orderLines;
//    }
//
//    public List<OrderLine> getAllOrderLinesOfOrder(Long barId, Long sessionId, Long orderId) throws NotFoundException {
//        List<Order> orders = this.ORDER_SERVICE.getAllOrdersOfSession(barId, sessionId);
//        List<OrderLine> orderLines = extractOrderLinesFromOrderWithId(orders, orderId);
//        if (orderLines.isEmpty()) throw new NotFoundException(String.format("No order lines found in session with id: %s", sessionId));
//        return orderLines;
//    }
//
//    public OrderLine getOrderLineOfOrder(Long barId, Long sessionId, Long orderId, Long orderLineId) throws NotFoundException {
//        Order order = this.ORDER_SERVICE.getOrderOfSession(barId, sessionId, orderId);
//        for (OrderLine orderLine : order.getOrderLines()) {
//            if (orderLine.getId().equals(orderLineId)) {
//                return orderLine;
//            }
//        }
//        throw new NotFoundException(String.format("No order line found in order with id: %s", orderId));
//    }

    public OrderLine setProductAmount(OrderLine orderLine, Integer amount) {
        if (amount == null) amount = 1;
        orderLine.setAmount(amount);
        return this.SPRING_ORDER_LINE_REPOSITORY.save(orderLine);
    }

    public OrderLine createOrderLine(Product product, Integer amount) {
        return new OrderLine(product, amount);
    }

    public boolean addProductToOrder(Long barId, Order order, Long productId, Integer amount) {
        if (amount == null) amount = 1;
        //Product product  = this.PRODUCT_SERVICE.getProductById(barId, productId)
        Product product  = null;
        return order.addOrderLine(createOrderLine(product, amount));
    }

    public void deleteOrderLine(OrderLine orderLine) {
        this.SPRING_ORDER_LINE_REPOSITORY.delete(orderLine);
    }
}
