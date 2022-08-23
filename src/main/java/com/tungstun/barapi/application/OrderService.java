package com.tungstun.barapi.application;

import com.tungstun.barapi.application.session.SessionQueryHandler;
import com.tungstun.barapi.application.session.bill.BillQueryHandler;
import com.tungstun.barapi.application.session.bill.query.GetBill;
import com.tungstun.barapi.application.session.query.GetSession;
import com.tungstun.barapi.application.session.query.ListSessionsOfBar;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final SessionQueryHandler sessionQueryHandler;
    private final BillQueryHandler billQueryHandler;
    private final PersonService personService;
    private final ProductService productService;

    public OrderService(SessionQueryHandler sessionQueryHandler, BillQueryHandler billQueryHandler, PersonService personService, ProductService productService) {
        this.sessionQueryHandler = sessionQueryHandler;
        this.billQueryHandler = billQueryHandler;
        this.personService = personService;
        this.productService = productService;
    }

    public List<Order> getAllOrdersOfBar(Long barId) throws EntityNotFoundException {
        return sessionQueryHandler.handle(new ListSessionsOfBar(barId))
                .stream()
                .map(Session::getAllOrders)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Order> getAllOrdersOfSession(Long barId, Long sessionId) throws EntityNotFoundException {
        return sessionQueryHandler.handle(new GetSession(sessionId, barId))
                .getAllOrders();
    }

//    public Order getOrderOfSession(Long barId, Long sessionId, UUID orderId) throws EntityNotFoundException {
//        return getAllOrdersOfSession(sessionId, barId)
//                .stream()
//                .filter(order -> order.getId().equals(orderId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("No order found with id " + orderId));
//    }

    public List<Order> getAllOrdersOfBill(Long barId, Long sessionId, UUID billId) throws EntityNotFoundException {
        return billQueryHandler.handle(new GetBill(billId, sessionId, barId))
                .getOrders();
    }

    public Order getOrderOfBill(Long barId, Long sessionId, UUID billId, UUID orderId) throws EntityNotFoundException {
        return getAllOrdersOfBill(barId, sessionId, billId)
                .stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No order found with id " + orderId));
    }

    public void deleteOrderFromBill(Long barId, Long sessionId, UUID billId, UUID orderId) throws EntityNotFoundException {
        billQueryHandler.handle(new GetBill(billId, sessionId, barId))
                .removeOrder(orderId);
        //todo Bill repository to save bill
    }

    public UUID addProductToBill(Long barId, Long sessionId, UUID billId, OrderRequest orderRequest, String username) throws EntityNotFoundException {
        Person bartender = personService.getPersonOfBar(barId, username);
        Product product = productService.getProductOfBar(barId, orderRequest.productId);
        return billQueryHandler.handle(new GetBill(billId, sessionId, barId))
                .addOrder(product, orderRequest.amount, bartender)
                .getId();
    }
}
