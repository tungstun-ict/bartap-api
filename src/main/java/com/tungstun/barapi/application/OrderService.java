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
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final SpringOrderRepository springOrderRepository;
    private final SpringBillRepository billRepository;
    private final ProductService productService;
    private final SessionService sessionService;
    private final BillService billService;
    private final BarService barService;


    public OrderService(SpringBillRepository billRepository,
                        SessionService sessionService,
                        BillService billService,
                        SpringOrderRepository springOrderRepository,
                        ProductService productService,
                        BarService barService
    ) {
        this.billRepository = billRepository;
        this.sessionService = sessionService;
        this.billService = billService;
        this.springOrderRepository = springOrderRepository;
        this.productService = productService;
        this.barService = barService;
    }

    public List<Order> getAllOrdersOfBar(Long barId) throws EntityNotFoundException {
        List<Session> sessions = this.sessionService.getAllSessionsOfBar(barId);
        return sessions.stream()
                .flatMap(session -> extractOrdersFromSession(session).stream())
                .collect(Collectors.toList());
    }

    private List<Order> extractOrdersFromSession(Session session) {
        return session.getBills().stream()
                .flatMap(bill -> bill.getOrders().stream())
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrdersOfSession(Long barId, Long sessionId) throws EntityNotFoundException {
        Session session = this.sessionService.getSessionOfBar(barId, sessionId);
        return extractOrdersFromSession(session);
    }

    public Order getOrderOfSession(Long barId, Long sessionId, Long orderId) throws EntityNotFoundException {
        Session session = this.sessionService.getSessionOfBar(barId, sessionId);
        return extractOrdersFromSession(session).stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with id: %s", orderId)));
    }

    public List<Order> getAllOrdersOfBill(Long barId, Long sessionId, Long billId) throws EntityNotFoundException {
        Bill bill = this.billService.getBillOfBar(barId, sessionId, billId);
        return bill.getOrders();
    }

    public Order getOrderOfBill(Long barId, Long sessionId, Long billId, Long orderId) throws EntityNotFoundException {
        Bill bill = this.billService.getBillOfBar(barId, sessionId, billId);
        return findOrderInBill(bill, orderId);
    }

    private Order findOrderInBill(Bill bill, Long orderId) throws EntityNotFoundException {
        return bill.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("No Order with id %s found in bill", orderId)));
    }

    public void deleteOrderFromBill(Long barId, Long sessionId, Long billId, Long orderId) throws EntityNotFoundException {
        Bill bill = this.billService.getBillOfBar(barId, sessionId, billId);
        if (bill.getSession().isLocked())
            throw new IllegalStateException("Cannot delete order from bill when session of bill is not active");
        Order order = findOrderInBill(bill, orderId);
        this.billService.removeOrderFromBill(bill, order);
        this.springOrderRepository.delete(order);
    }

    public Bill addProductToBill(Long barId, Long sessionId, Long billId, OrderRequest orderRequest, String username) throws EntityNotFoundException {
        Bill bill = this.billService.getBillOfBar(barId, sessionId, billId);
        this.sessionService.checkEditable(bill.getSession());
        Person bartender = findPersonOfUser(barId, username);
        Product product = this.productService.getProductOfBar(barId, orderRequest.productId);
        bill.addOrder(product, orderRequest.amount, bartender);
        return billRepository.save(bill);
    }

    private Person findPersonOfUser(Long barId, String username) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        return bar.getPeople().stream()
                .filter(person -> person.getUser() != null)
                .filter(person -> person.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No person found connected to you user account"));
    }
}
