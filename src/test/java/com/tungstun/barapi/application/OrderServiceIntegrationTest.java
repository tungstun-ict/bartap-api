package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.data.SpringSessionRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.BillFactory;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.*;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.security.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderServiceIntegrationTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringBillRepository billRepository;
    @Autowired
    private OrderService service;

    private Bar bar;
    private Category category;
    private Product product;
    private Session session;
    private Session session2;
    private Session session3;
    private Person customer;
    private Bill bill;
    private Bill bill2;
    private Bill bill3;
    private Order order;
    private Order order2;

    @BeforeEach
    void setup() {
        bar = new BarBuilder().build();
        category = categoryRepository.save(new Category("Drinks", ProductType.DRINK));
        bar.addCategory(category);
        product = new ProductBuilder(123L, "product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();
        bar.addProduct(product);
        customer = new PersonBuilder().setName("testPerson").build();
        session = sessionRepository.save(Session.create("test"));
        bill = new BillFactory(session, customer).create();
        bill.addOrder(product, 1, customer);
        bill = billRepository.save(bill);
        session.addBill(bill);
        session = sessionRepository.save(session);
        session2 = sessionRepository.save(Session.create("test2"));
        bill2 = new BillFactory(session2, customer).create();
        bill2.addOrder(product, 1, customer);
        bill2 = billRepository.save(bill2);
        session2.addBill(bill2);
        session2 = sessionRepository.save(session2);
        session3 = sessionRepository.save(Session.create("test3"));
        bill3 = billRepository.save(new BillFactory(session3, customer).create());
        session3.addBill(bill3);
        session3 = sessionRepository.save(session3);
        bill3 = session3.getBills().stream().findFirst().get();
        bar.addSession(session);
        bar.addSession(session2);
        bar.addSession(session3);
        order = bill.getOrders().stream().findFirst().get();
        order2 = bill2.getOrders().stream().findFirst().get();
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get all orders of bar")
    void getAllOrdersOfBar() throws EntityNotFoundException {
        List<Order> resOrders = service.getAllOrdersOfBar(bar.getId());

        assertEquals(2, resOrders.size());
        assertTrue(resOrders.contains(order));
        assertTrue(resOrders.contains(order2));
    }

    @Test
    @DisplayName("Get none orders of bar")
    void getNoneOrdersOfBar() throws EntityNotFoundException {
        Bar bar2 = barRepository.save(new BarBuilder().build());

        List<Order> resOrders = service.getAllOrdersOfBar(bar2.getId());

        assertEquals(0, resOrders.size());
    }

    @Test
    @DisplayName("Get all orders of session")
    void getAllOrdersOfSession() throws EntityNotFoundException {
        List<Order> resOrders = service.getAllOrdersOfSession(bar.getId(), session.getId());

        assertEquals(1, resOrders.size());
        assertTrue(resOrders.contains(order));
    }

    @Test
    @DisplayName("Get none orders of session")
    void getNoneOrdersOfSession() throws EntityNotFoundException {
        Bar bar2 = new BarBuilder().build();
        bar2.addSession(Session.create("test"));
        bar2 = barRepository.save(bar2);
        Session session2 = bar2.getSessions().stream().findFirst().get();

        List<Order> resOrders = service.getAllOrdersOfSession(bar2.getId(), session2.getId());

        assertEquals(0, resOrders.size());
    }

    @Test
    @DisplayName("Get order of session")
    void getOrderOfSession() throws EntityNotFoundException {
        Order resOrder = service.getOrderOfSession(bar.getId(), session.getId(), order.getId());

        assertEquals(order, resOrder);
    }

    @Test
    @DisplayName("Get not existing order of session")
    void getNotExistingOrderOfSession() {
        assertThrows(
                EntityNotFoundException.class,
                () -> service.getOrderOfSession(bar.getId(), session.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Get all orders of bill")
    void getAllOrdersOfBill() throws EntityNotFoundException {
        List<Order> resOrders = service.getAllOrdersOfBill(bar.getId(), session.getId(), bill.getId());

        assertEquals(1, resOrders.size());
        assertTrue(resOrders.contains(order));
    }

    @Test
    @DisplayName("Get none orders of bill")
    void getNoneOrdersOfBill() throws EntityNotFoundException {
        Bar bar2 = new BarBuilder().build();
        Session session2 = Session.create("test");
        session2.addBill(new BillFactory(session2, customer).create());
        bar2.addSession(session2);
        bar2 = barRepository.save(bar2);
        session2 = bar2.getSessions().stream().findFirst().get();
        Bill bill2 = session2.getBills().stream().findFirst().get();

        List<Order> resOrders = service.getAllOrdersOfBill(bar2.getId(), session2.getId(), bill2.getId());

        assertEquals(0, resOrders.size());
    }

    @Test
    @DisplayName("Get order of bill")
    void getOrderOfBill() throws EntityNotFoundException {
        Order resOrder = service.getOrderOfBill(bar.getId(), session.getId(), bill.getId(), order.getId());

        assertEquals(order, resOrder);
    }

    @Test
    @DisplayName("Get not existing order of bill")
    void getNotExistingOrderOfBill() {
        assertThrows(
                EntityNotFoundException.class,
                () -> service.getOrderOfBill(bar.getId(), session.getId(), bill.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Delete order from bill")
    void deleteOrderFromBill() throws EntityNotFoundException {
        service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), order.getId());

        bill = billRepository.findById(bill.getId()).get();

        assertTrue(bill.getOrders().isEmpty());
    }

    @Test
    @DisplayName("Delete not existing order from bill")
    void deleteNotExistingOrderFromBill() {
        assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Delete order from of inactive session")
    void deleteOrderFromBillOfInactiveSession() {
        bill = billRepository.findById(bill.getId()).get();
        bill.getSession().lock();
        billRepository.save(bill);

        assertThrows(
                IllegalStateException.class,
                () -> service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), order.getId())
        );
    }

    @Test
    @DisplayName("Add product(order) to bill")
    void addOrderToBill() throws EntityNotFoundException {
        User user = new User("testUser", "", "", "", "", new ArrayList<>());
        Person person = new PersonBuilder().setUser(user).build();
        OrderRequest request = new OrderRequest();
        request.amount = 2;
        request.productId = product.getId();
        bar.addUser(person);
        barRepository.save(bar);

        Bill resBill = service.addProductToBill(bar.getId(), session.getId(), bill.getId(), request, "testUser");

        assertTrue(resBill.getOrders().stream().anyMatch(o -> o.getAmount() == 2));
    }

    @Test
    @DisplayName("Add product(order) to bill with not coupled user/person to bar")
    void addOrderToBillNotCoupledPerson() throws EntityNotFoundException {
        OrderRequest request = new OrderRequest();
        request.amount = 2;
        request.productId = product.getId();

        assertThrows(
                EntityNotFoundException.class,
                () -> service.addProductToBill(bar.getId(), session.getId(), bill.getId(), request, "notExisting")
        );
    }
}