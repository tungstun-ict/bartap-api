package com.tungstun.barapi.application;

import com.tungstun.barapi.application.order.OrderCommandHandler;
import com.tungstun.barapi.application.order.OrderQueryHandler;
import com.tungstun.barapi.application.order.command.AddOrder;
import com.tungstun.barapi.application.order.command.RemoveOrder;
import com.tungstun.barapi.application.order.query.GetOrder;
import com.tungstun.barapi.application.order.query.ListOrdersOfBill;
import com.tungstun.barapi.application.order.query.ListOrdersOfSession;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.Order;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.bill.SpringBillRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
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
class OrderCommandHandlerIntegrationTest {
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringBillRepository billRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringUserRepository userRepository;
    @Autowired
    private OrderQueryHandler orderQueryHandler;
    @Autowired
    private OrderCommandHandler service;

    private Bar bar;
    private Product product;
    private Session session;
    private Session session3;
    private User user;
    private Bill bill;
    private Bill bill3;
    private Order order;

    @BeforeEach
    void setup() {
        Category category = new CategoryFactory("Drinks").create();

        product = new ProductBuilder("product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();

        user = new User("testUser", "", "", "", "", "+310612345678", new ArrayList<>());
        userRepository.save(user);
        Person customer = personRepository.save(new PersonBuilder("name")
                .setName("testPerson")
                .setUser(user)
                .build());

        session = new Session(UUID.randomUUID(), "test", new ArrayList<>());
        bill = session.addCustomer(customer);
        order = bill.addOrder(product, 1, customer);

        Session session2 = new Session(UUID.randomUUID(), "test2", new ArrayList<>());
        Bill bill2 = session2.addCustomer(customer);
        bill2.addOrder(product, 1, customer);

        session3 = new Session(UUID.randomUUID(), "test3", new ArrayList<>());
        bill3 = session3.addCustomer(customer);

        bar = barRepository.save(new BarBuilder("bar")
                .setCategories(new ArrayList<>(List.of(category)))
                .setProducts(new ArrayList<>(List.of(product)))
                .setPeople(new ArrayList<>(List.of(customer)))
                .setSessions(new ArrayList<>(List.of(session, session2, session3)))
                .build());
    }

//    @Test
//    @DisplayName("Get all orders of bar")
//    void getAllOrdersOfBar() throws EntityNotFoundException {
//        List<Order> resOrders = service.getAllOrdersOfBar(bar.getId());
//
//        assertEquals(2, resOrders.size());
//        assertTrue(resOrders.contains(order));
//        assertTrue(resOrders.contains(order2));
//    }

//    @Test
//    @DisplayName("Get none orders of bar")
//    void getNoneOrdersOfBar() throws EntityNotFoundException {
//        Bar bar2 = barRepository.save(new BarBuilder("name").build());
//
//        List<Order> resOrders = service.getAllOrdersOfBar(bar2.getId());
//
//        assertEquals(0, resOrders.size());
//    }

    @Test
    @DisplayName("Get all orders of session")
    void getAllOrdersOfSession() throws EntityNotFoundException {
        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfSession(session.getId(), bar.getId()));

        assertEquals(1, resOrders.size());
        assertTrue(resOrders.stream().anyMatch(o -> o.getId().equals(order.getId())));
    }

    @Test
    @DisplayName("Get none orders of session")
    void getNoneOrdersOfSession() throws EntityNotFoundException {
//        Bar bar2 = new BarBuilder().build();
//        bar2.addSession(Session.create(123L, "test"));
//        bar2 = barRepository.save(bar2);
//        Session session2 = bar2.getSessions().stream().findFirst().get();

        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfSession(session3.getId(), bar.getId()));

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
        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfBill(bill.getId(), session.getId(), bar.getId()));

        assertEquals(1, resOrders.size());
        assertTrue(resOrders.stream().anyMatch(o -> o.getId().equals(order.getId())));
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

        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfBill(bill3.getId(), session3.getId(), bar.getId()));

        assertEquals(0, resOrders.size());
    }

    @Test
    @DisplayName("Get order of bill")
    void getOrderOfBill() throws EntityNotFoundException {
        Order resOrder = orderQueryHandler.handle(new GetOrder(order.getId(), bill.getId(), session.getId(), bar.getId()));

        assertEquals(order.getId(), resOrder.getId());
    }

    @Test
    @DisplayName("Get not existing order of bill")
    void getNotExistingOrderOfBill() {
        assertThrows(
                EntityNotFoundException.class,
                () -> orderQueryHandler.handle(new GetOrder(UUID.randomUUID(), bill.getId(), session.getId(), bar.getId()))
        );
    }

    @Test
    @DisplayName("Add product(order) to bill")
    void addOrderToBill() throws EntityNotFoundException {
        AddOrder command = new AddOrder(
                bar.getId(),
                session.getId(),
                bill.getId(),
                product.getId(),
                2,
                user.getUsername()
        );

        UUID orderId = service.addProductToBill(command);

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
        AddOrder command = new AddOrder(
                bar.getId(),
                session.getId(),
                bill.getId(),
                product.getId(),
                2,
                "notExistingUsername"
        );
        assertThrows(
                EntityNotFoundException.class,
                () -> service.addProductToBill(command)
        );
    }

    @Test
    @DisplayName("Delete order from bill")
    void deleteOrderFromBill() throws EntityNotFoundException {
        RemoveOrder command = new RemoveOrder(bar.getId(), session.getId(), bill.getId(), order.getId());

        service.deleteOrderFromBill(command);

        bill = billRepository.findById(bill.getId()).orElseThrow();
        assertTrue(bill.getOrders().isEmpty());
    }

    @Test
    @DisplayName("Delete not existing order from bill")
    void deleteNotExistingOrderFromBill() {
        RemoveOrder command = new RemoveOrder(bar.getId(), session.getId(), bill.getId(), UUID.randomUUID());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteOrderFromBill(command)
        );
    }
}
