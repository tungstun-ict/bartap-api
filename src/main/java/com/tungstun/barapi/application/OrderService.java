package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderRepository;
import com.tungstun.barapi.domain.*;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final SpringOrderRepository SPRING_ORDER_REPOSITORY;
    private final SessionService SESSION_SERVICE;
    private final PersonService PERSON_SERVICE;
    private final BillService BILL_SERVICE;

    public OrderService(SpringOrderRepository springOrderRepository, SessionService sessionService, PersonService personService, BillService billService) {
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
        this.SESSION_SERVICE = sessionService;
        this.PERSON_SERVICE = personService;
        this.BILL_SERVICE = billService;
    }

    private List<Order> extractOrdersFromSession(Session session) {
        List<Order> orders = new ArrayList<>();
        for (Bill bill : session.getBills()) {
            orders.addAll(bill.getOrders());
        }
        return orders;
    }

    private Order findOrderInSession(Session session, Long orderId) throws NotFoundException {
        List<Order> orders = extractOrdersFromSession(session);
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        throw new NotFoundException(String.format("No order found with id: %s", orderId));
    }

    private Bartender findBartenderInBar(Long barId, Long bartenderId) throws NotFoundException {
        Person person = this.PERSON_SERVICE.getPersonOfBar(barId, bartenderId);
        if (!(person instanceof Bartender)) throw new NotFoundException(String.format("No Bartender found with given id %s", bartenderId));
        return (Bartender) person;
    }

    private Bill findBillInSessionOfCustomer(Session session, Long customerId) throws NotFoundException {
        for(Bill iterateBill : session.getBills()) {
            if (iterateBill.getCustomer().getId().equals(customerId)) {
                return iterateBill;
            }
        }
        throw new NotFoundException(String.format("No bill found for customer with id %s", customerId));
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
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        List<Order> orders = extractOrdersFromSession(session);
        if (orders.isEmpty()) throw new NotFoundException(String.format("No orders found for session with id: %s", sessionId));
        return orders;
    }

    public Order getOrderOfSession(Long barId, Long sessionId, Long orderId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        return findOrderInSession(session, orderId);
    }

    public Order createNewOrderForSession(Long barId, Long sessionId, OrderRequest orderRequest) throws NotFoundException {
        Bartender bartender = findBartenderInBar(barId, orderRequest.bartenderId);
        Order order = new OrderFactory(LocalDateTime.now(), bartender).create();

        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        Bill bill = findBillInSessionOfCustomer(session, orderRequest.customerId);
        bill.getOrders().add(order);
        this.BILL_SERVICE.saveBill(bill);

        return this.SPRING_ORDER_REPOSITORY.save(order);
    }
}
