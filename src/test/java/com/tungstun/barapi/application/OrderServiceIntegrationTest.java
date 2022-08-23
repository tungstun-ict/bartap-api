package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringBillRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.barapi.domain.payment.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.exceptions.InvalidSessionStateException;
import com.tungstun.barapi.port.persistence.category.SpringCategoryRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
import com.tungstun.barapi.port.persistence.product.SpringProductRepository;
import com.tungstun.barapi.port.persistence.session.SpringSessionRepository;
import com.tungstun.barapi.presentation.dto.request.OrderRequest;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.port.persistence.user.SpringUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {
    @Autowired
    private SpringCategoryRepository categoryRepository;
    @Autowired
    private SpringSessionRepository sessionRepository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringProductRepository productRepository;
    @Autowired
    private SpringBillRepository billRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringUserRepository userRepository;
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
        bar = barRepository.save(new BarBuilder().build());
//        bar = barRepository.findById(bar.getId()).orElseThrow();

        category = categoryRepository.save(new Category(bar.getId(), "Drinks", ProductType.DRINK));
        category = categoryRepository.getById(category.getId());
        bar.addCategory(category);

        product = new ProductBuilder(bar.getId(), "product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();
        product = productRepository.save(product);
        bar.addProduct(product);

        User user = new User("testUser", "", "", "", "", "+310612345678", new ArrayList<>());
        userRepository.save(user);
        customer = personRepository.save(new PersonBuilder(bar.getId(), "name")
                .setName("testPerson")
                .setUser(user)
                .build());

        session = Session.create(bar.getId(), "test");
        bill = session.addCustomer(customer);
//        bill = new BillFactory(session, customer).create();
        order = bill.addOrder(product, 1, customer);
//        bill = billRepository.save(bill);
//        session.addBill(bill);
        session = sessionRepository.save(session);

        session2 = Session.create(bar.getId(), "test2");
        bill2 = session2.addCustomer(customer);
//        bill2 = new BillFactory(session2, customer).create();
        order2 = bill2.addOrder(product, 1, customer);
//        bill2 = billRepository.save(bill2);
//        session2.addBill(bill2);
        session2 = sessionRepository.save(session2);

        session3 = Session.create(bar.getId(), "test3");
        bill3 = session3.addCustomer(customer);
//        bill3 = billRepository.save(new BillFactory(session3, customer).create());
//        session3.addBill(bill3);
        session3 = sessionRepository.save(session3);
//        bill3 = session3.getBills().stream().findFirst().get();

        bar.addSession(session);
        bar.addSession(session2);
        bar.addSession(session3);
//        order = bill.getOrders().stream().findFirst().get();
//        order2 = bill2.getOrders().stream().findFirst().get();
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
//        Bar bar2 = new BarBuilder().build();
//        bar2.addSession(Session.create(123L, "test"));
//        bar2 = barRepository.save(bar2);
//        Session session2 = bar2.getSessions().stream().findFirst().get();

        List<Order> resOrders = service.getAllOrdersOfSession(bar.getId(), session3.getId());

        assertEquals(0, resOrders.size());
    }

//    @Test
//    @DisplayName("Get order of session")
//    void getOrderOfSession() throws EntityNotFoundException {
//        Order resOrder = service.getorder(bar.getId(), session.getId(), order.getId());
//
//        assertEquals(order, resOrder);
//    }

//    @Test
//    @DisplayName("Get not existing order of session")
//    void getNotExistingOrderOfSession() {
//        assertThrows(
//                EntityNotFoundException.class,
//                () -> service.getOrderOfSession(bar.getId(), session.getId(), 999L)
//        );
//    }

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
//        Bar bar2 = new BarBuilder().build();
//        Session session2 = Session.create(123L, "test");
//        session2.addBill(new BillFactory(session2, customer).create());
//        bar2.addSession(session2);
//        bar2 = barRepository.save(bar2);
//        session2 = bar2.getSessions().stream().findFirst().get();
//        Bill bill2 = session2.getBills().stream().findFirst().get();

        List<Order> resOrders = service.getAllOrdersOfBill(bar.getId(), session3.getId(), bill3.getId());

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
                () -> service.getOrderOfBill(bar.getId(), session.getId(), bill.getId(), UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Delete order from bill")
    void deleteOrderFromBill() throws EntityNotFoundException {
        service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), order.getId());

        bill = billRepository.findById(bill.getId()).get();

        assertTrue(bill.getOrders().isEmpty());
    }

//    @Test
//    @DisplayName("Delete not existing order from bill")
//    void deleteNotExistingOrderFromBill() {
//        assertThrows(
//                EntityNotFoundException.class,
//                () -> service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), UUID.randomUUID())
//        );
//    }

    @Test
    @DisplayName("Delete order from of inactive session")
    void deleteOrderFromBillOfInactiveSession() {
        Session testSession = sessionRepository.getById(session.getId());
        testSession.lock();
        testSession = sessionRepository.save(testSession);

//        bill = billRepository.findById(bill.getId()).get();
//        bill.getSession().lock();
//        billRepository.save(bill);

        assertThrows(
                InvalidSessionStateException.class,
                () -> service.deleteOrderFromBill(bar.getId(), session.getId(), bill.getId(), order.getId())
        );
    }

    @Test
    @DisplayName("Add product(order) to bill")
    void addOrderToBill() throws EntityNotFoundException {
//        User user = new User("testUser", "", "", "", "", "+310612345678", new ArrayList<>());
//        userRepository.save(user);
//        Person person = new PersonBuilder(123L, "name")
//                .setUser(user)
//                .build();
//        personRepository.save(person);
//        System.out.println(personRepository.findAll());
        OrderRequest request = new OrderRequest();
        request.amount = 2;
        request.productId = product.getId();
        bar.addPerson(customer);
        barRepository.save(bar);

        sessionRepository.findAll().forEach(s -> System.out.println(s.getId()));

        UUID orderId = service.addProductToBill(bar.getId(), session.getId(), bill.getId(), request, "testUser");

        boolean exists = billRepository.getById(bill.getId())
                .getOrders()
                .stream()
                .filter(order -> order.getId().equals(orderId))
                .anyMatch(o -> o.getAmount() == 2);
        assertTrue(exists);
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