package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderRepository;
import com.tungstun.barapi.domain.*;
import com.tungstun.barapi.presentation.dto.request.OrderLineRequest;
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
    private final OrderLineService ORDER_LINE_SERVICE;


    public OrderService(SpringOrderRepository springOrderRepository, SessionService sessionService,
                        PersonService personService, BillService billService, OrderLineService orderLineService) {
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
        this.SESSION_SERVICE = sessionService;
        this.PERSON_SERVICE = personService;
        this.BILL_SERVICE = billService;
        this.ORDER_LINE_SERVICE = orderLineService;
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

    private Bill getBillWithOrder(Session session, Long orderId) throws NotFoundException {
        for(Bill bill : session.getBills()) {
            for(Order order : bill.getOrders()) {
                if (order.getId().equals(orderId)){
                    return bill;
                }
            }
        }
        throw new NotFoundException(String.format("Could not found a bill with order id %s", orderId));
    }

    private Order findOrderInBill(Bill bill, Long orderId) throws NotFoundException {
        for(Order order : bill.getOrders()) {
            if (order.getId().equals(orderId)){
                return order;
            }
        }
        throw new NotFoundException(String.format("No Order with id %s found in bill", orderId));
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
        order = this.SPRING_ORDER_REPOSITORY.save(order);

        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        Bill bill = findBillInSessionOfCustomer(session, orderRequest.customerId);
        this.BILL_SERVICE.addOrderToBill(bill, order);

        return order;
    }

    public void deleteOrderFromSession(Long barId, Long sessionId, Long orderId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        Bill bill = getBillWithOrder(session, orderId);
        Order order = findOrderInBill(bill, orderId);
        this.BILL_SERVICE.removeOrderFromBill(bill, order);
    }



    private OrderLine checkIfOrderContainsProduct(Order order, Long productId) {
        for (OrderLine orderLine : order.getOrderLines()){
            //todo: error as long as product is not related to orderline
//            if (orderLine.getProduct().getId().equals(productId)){
//                return orderLine;
//            }
            return orderLine;
        }
        return null;
    }

    public Order addProductToOrder(Long barId, Long sessionId, Long orderId, OrderLineRequest orderLineRequest) throws NotFoundException {
        Order order = getOrderOfSession(barId, sessionId, orderId);
        OrderLine orderLine = checkIfOrderContainsProduct(order, orderLineRequest.productId);
        if (orderLine == null) {
            this.ORDER_LINE_SERVICE.addProductToOrder(barId, order, orderLineRequest.productId, orderLineRequest.amount);
        }
        else{
            this.ORDER_LINE_SERVICE.setProductAmount(orderLine, orderLineRequest.amount);
        }
        //todo: update price of order OF misschien beter, het verwijderen van price, want dat is gewoon te berekenen, maar dan wel de price toevoegen aan response
        return this.SPRING_ORDER_REPOSITORY.save(order);
    }

    public Order deleteProductFromOrder(Long barId, Long sessionId, Long orderId, Long productId) throws NotFoundException {
        Order order = getOrderOfSession(barId, sessionId, orderId);
        OrderLine orderLine = checkIfOrderContainsProduct(order, productId);
        order.removeOrderLine(orderLine);
        this.ORDER_LINE_SERVICE.deleteOrderLine(orderLine);
        return this.SPRING_ORDER_REPOSITORY.save(order);
    }
}
