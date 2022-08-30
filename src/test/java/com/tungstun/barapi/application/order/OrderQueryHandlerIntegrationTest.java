package com.tungstun.barapi.application.order;

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
class OrderQueryHandlerIntegrationTest {
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringUserRepository userRepository;
    @Autowired
    private OrderQueryHandler orderQueryHandler;

    private Bar bar;
    private Session session;
    private Session session3;
    private Bill bill;
    private Bill bill3;
    private Order order;

    @BeforeEach
    void setup() {
        Category category = new CategoryFactory("Drinks").create();

        Product product = new ProductBuilder("product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();

        User user = new User("testUser", "", "", "", "", "+310612345678", new ArrayList<>());
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
        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfSession(session3.getId(), bar.getId()));

        assertEquals(0, resOrders.size());
    }

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
        List<Order> resOrders = orderQueryHandler.handle(new ListOrdersOfBill(bill3.getId(), session3.getId(), bar.getId()));

        assertEquals(0, resOrders.size());
    }

    @Test
    @DisplayName("Get order of bill")
    void getOrderOfBill() throws EntityNotFoundException {
        Order resOrder = orderQueryHandler.handle(new GetOrder(bar.getId(), session.getId(), bill.getId(), order.getId()));

        assertEquals(order.getId(), resOrder.getId());
    }

    @Test
    @DisplayName("Get not existing order of bill")
    void getNotExistingOrderOfBill() {
        assertThrows(
                EntityNotFoundException.class,
                () -> orderQueryHandler.handle(new GetOrder(bar.getId(), session.getId(), bill.getId(), UUID.randomUUID()))
        );
    }
}
