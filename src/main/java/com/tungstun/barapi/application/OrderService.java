package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.data.SpringOrderRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.security.application.UserService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final SpringOrderRepository SPRING_ORDER_REPOSITORY;
    private final SpringBillRepository BILL_REPOSITORY;
    private final ProductService PRODUCT_SERVICE;
    private final SessionService SESSION_SERVICE;
    private final BillService BILL_SERVICE;
    private final UserService USER_SERVICE;
    private final BarService BAR_SERVICE;


    public OrderService(SpringBillRepository billRepository,
                        SessionService sessionService,
                        BillService billService,
                        SpringOrderRepository springOrderRepository,
                        ProductService productService,
                        UserService userService,
                        BarService barService
    ) {
        this.BILL_REPOSITORY = billRepository;
        this.SESSION_SERVICE = sessionService;
        this.BILL_SERVICE = billService;
        this.SPRING_ORDER_REPOSITORY = springOrderRepository;
        this.PRODUCT_SERVICE = productService;
        this.USER_SERVICE = userService;
        this.BAR_SERVICE = barService;
    }

    private Order findOrderInBill(Bill bill, Long orderId) throws NotFoundException {
        return bill.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No Order with id %s found in bill", orderId)));
    }

    public List<Order> getAllOrdersOfBar(Long barId) throws NotFoundException {
        List<Session> sessions = this.SESSION_SERVICE.getAllSessionsOfBar(barId);
        return sessions.stream()
                .flatMap(session -> extractOrdersFromSession(session).stream())
                .collect(Collectors.toList());
    }

    private List<Order> extractOrdersFromSession(Session session) {
        return session.getBills().stream()
                .flatMap(bill -> bill.getOrders().stream())
                .collect(Collectors.toList());
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

    private Order findOrderInSession(Session session, Long orderId) throws NotFoundException {
        return extractOrdersFromSession(session).stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("No order found with id: %s", orderId)));
    }

    public List<Order> getAllOrdersOfBill(Long barId, Long sessionId, Long billId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        List<Order> orders = bill.getOrders();
        if (orders.isEmpty()) throw new NotFoundException(String.format("No orders found for bill with id '%s'", billId));
        return orders;
    }

    public Order getOrderOfBill(Long barId, Long sessionId, Long billId, Long orderId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        return bill.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("No order found with id '%s' in bill with id '%s'", orderId, billId)));
    }

    public void deleteOrderFromBill(Long barId, Long sessionId, Long billId, Long orderId) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        if (!bill.getSession().isActive())
            throw new IllegalStateException("Cannot delete order from bill when session of bill is not active");
        Order order = findOrderInBill(bill, orderId);
        this.BILL_SERVICE.removeOrderFromBill(bill, order);
        this.SPRING_ORDER_REPOSITORY.delete(order);
    }

    public Bill addProductToBill(Long barId, Long sessionId, Long billId, OrderRequest orderRequest, String username) throws NotFoundException {
        Bill bill = this.BILL_SERVICE.getBillOfBar(barId, sessionId, billId);
        this.SESSION_SERVICE.sessionIsActive(bill.getSession());
        Person bartender = findPersonOfUser(barId, username);
        Product product = this.PRODUCT_SERVICE.getProductOfBar(barId, orderRequest.productId);
        bill.addOrder(product, orderRequest.amount, bartender);
        return BILL_REPOSITORY.save(bill);
    }

    private Person findPersonOfUser(Long barId, String username) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getUsers().stream()
                .filter(person -> person.getUser() != null)
                .filter(person -> person.getUser().getUsername().equals(username))
                .findAny()
                .orElseThrow(() -> new NotFoundException("No person found connected to you user account"));
    }
}
