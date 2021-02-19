package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringOrderRepository;
import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.order.Order;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final ProductService PRODUCT_SERVICE;
    private final SpringOrderRepository SPRING_ORDER_REPOSITORY;
    private final SessionService SESSION_SERVICE;
    private final PersonService PERSON_SERVICE;
    private final BillService BILL_SERVICE;


    public OrderService(SessionService sessionService,
                        PersonService personService,
                        BillService billService,
                        SpringOrderRepository springOrderRepository,
                        ProductService productService
    ) {
        this.SESSION_SERVICE = sessionService;
        this.PERSON_SERVICE = personService;
        this.BILL_SERVICE = billService;
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
        this.PRODUCT_SERVICE = productService;
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

    private Order findOrderInBill(Bill bill, Long orderId) throws NotFoundException {
        for (Order order : bill.getOrders()) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        throw new NotFoundException(String.format("No Order with id %s found in bill", orderId));
    }

    public List<Order> getAllOrdersOfBar(Long barId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        List<Order> orders = new ArrayList<>();
        for (Session session : sessions) {
            orders.addAll(extractOrdersFromSession(session));
        }
        if (orders.isEmpty()) throw new NotFoundException(String.format("No orders found for bar with id: %s", barId));
        return orders;
    }

    public List<Order> getAllOrdersOfSession(Long barId, Long sessionId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        List<Order> orders = extractOrdersFromSession(session);
        if (orders.isEmpty())
            throw new NotFoundException(String.format("No orders found for session with id: %s", sessionId));
        return orders;
    }

    public Order getOrderOfSession(Long barId, Long sessionId, Long orderId) throws NotFoundException {
        Session session = this.SESSION_SERVICE.getSessionOfBar(barId, sessionId);
        return findOrderInSession(session, orderId);
    }

    public List<Order> getAllOrdersOfBill(Long barId, Long sessionId, Long billId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        List<Order> orders = bill.getOrders();
        if (orders.isEmpty())
            throw new NotFoundException(String.format("No orders found for bill with id '%s'", billId));
        return orders;
    }

    public Order getOrderOfBill(Long barId, Long sessionId, Long billId, Long orderId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        for (Order order : bill.getOrders()) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        throw new NotFoundException(String.format("No order found with id '%s' in bill with id '%s'", orderId, billId));
    }

    public void deleteOrderFromBill(Long barId, Long sessionId, Long billId, Long orderId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        Order order = findOrderInBill(bill, orderId);
        this.BILL_SERVICE.removeOrderFromBill(bill, order);
    }

    private Bartender findBartenderInSession(Session session, Long bartenderId) throws NotFoundException {
        for (Bartender bartender : session.getBartenders()) {
            if (bartender.getId().equals(bartenderId)) return bartender;
        }
        throw new NotFoundException(String.format("No bartender found with id %s in session", bartenderId));
    }

    private Order buildOrder(Long barId, OrderRequest orderRequest, Bill bill) throws NotFoundException {
        int amount = (orderRequest.amount == null) ? 1 : orderRequest.amount;
        Bartender bartender = findBartenderInSession(bill.getSession(), orderRequest.bartenderId);
        Product product = this.PRODUCT_SERVICE.getProductOfBar(barId, orderRequest.productId);
        return new Order(product, amount, bartender);
    }

    private Order addOrderToBill(Bill bill, Order order) {
        order = this.SPRING_ORDER_REPOSITORY.save(order);
        this.BILL_SERVICE.addOrderToBill(bill, order);
        return order;
    }

    public Order addProductToBill(Long barId, Long sessionId, Long billId, OrderRequest orderRequest) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        Order order = buildOrder(barId, orderRequest, bill);
        return addOrderToBill(bill, order);
    }
}
