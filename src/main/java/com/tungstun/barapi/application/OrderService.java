package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderRepository;
import com.tungstun.barapi.domain.Bill;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.domain.Session;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final SpringOrderRepository SPRING_ORDER_REPOSITORY;
    private final SessionService SESSION_SERVICE;

    public OrderService(SpringOrderRepository springOrderRepository, SessionService sessionService) {
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
        SESSION_SERVICE = sessionService;
    }

    private List<Order> extractOrdersFromSession(Session session) {
        List<Order> orders = new ArrayList<>();
        for (Bill bill : session.getBills()) {
            orders.addAll(bill.getOrders());
        }
        return orders;
    }

    public List<Order> getAllOrdersOfBar(Long barId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        List<Order> orders = new ArrayList<>();
        for (Session session : sessions){
            orders.addAll(extractOrdersFromSession(session));
        }
        if (orders.isEmpty()) throw new NotFoundException(String.format("No orders found for bar with id: %s", barId));
        return orders;
    }

    public List<Order> getAllOrdersOfSession(Long barId, Long sessionId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        List<Order> orders = new ArrayList<>();
        for (Session session : sessions){
            if (session.getId().equals(sessionId)){
                orders.addAll(extractOrdersFromSession(session));
            }
        }
        if (orders.isEmpty()) throw new NotFoundException(String.format("No orders found for session with id: %s", sessionId));
        return orders;
    }
}
