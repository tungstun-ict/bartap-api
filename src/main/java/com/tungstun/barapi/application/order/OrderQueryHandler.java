package com.tungstun.barapi.application.order;

import com.tungstun.barapi.application.bill.BillQueryHandler;
import com.tungstun.barapi.application.bill.query.GetBill;
import com.tungstun.barapi.application.order.query.GetOrder;
import com.tungstun.barapi.application.order.query.ListOrdersOfBill;
import com.tungstun.barapi.application.order.query.ListOrdersOfSession;
import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.domain.bill.Order;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderQueryHandler {
    private final SessionQueryHandler sessionQueryHandler;
    private final BillQueryHandler billQueryHandler;

    public OrderQueryHandler(SessionQueryHandler sessionQueryHandler, BillQueryHandler billQueryHandler) {
        this.sessionQueryHandler = sessionQueryHandler;
        this.billQueryHandler = billQueryHandler;
    }

    public Order handle(GetOrder query) {
        return billQueryHandler.handle(new GetBill(query.barId(), query.sessionId(), query.billId()))
                .getOrders()
                .stream()
                .filter(order -> order.getId().equals(query.orderId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No Order found with id: " + query.orderId()));
    }

    public List<Order> handle(ListOrdersOfBill query) {
        return billQueryHandler.handle(new GetBill(query.barId(), query.sessionId(), query.billId()))
                .getOrders();
    }

    public List<Order> handle(ListOrdersOfSession query) {
        return sessionQueryHandler.handle(new GetSession(query.sessionId(), query.barId()))
                .getAllOrders();
    }
}
