package com.tungstun.barapi.application.order;

import com.tungstun.barapi.application.order.command.AddOrder;
import com.tungstun.barapi.application.order.command.RemoveOrder;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private OrderCommandHandler service;

    private Bar bar;
    private Product product;
    private Session session;
    private User user;
    private Bill bill;
    private Order order;

    @BeforeEach
    void setup() {
        Category category = new CategoryFactory("Drinks").create();

        product = new ProductBuilder("product", category)
                .setPrice(1.0)
                .setSize(100)
                .build();

        user = new User(UUID.randomUUID(), "testUser", "", "", "", "", "+310612345678", new ArrayList<>());
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

        Session session3 = new Session(UUID.randomUUID(), "test3", new ArrayList<>());
        session3.addCustomer(customer);

        bar = barRepository.save(new BarBuilder("bar")
                .setCategories(new ArrayList<>(List.of(category)))
                .setProducts(new ArrayList<>(List.of(product)))
                .setPeople(new ArrayList<>(List.of(customer)))
                .setSessions(new ArrayList<>(List.of(session, session2, session3)))
                .build());
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

        UUID orderId = service.handle(command);

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
                () -> service.handle(command)
        );
    }

    @Test
    @DisplayName("Delete order from bill")
    void deleteOrderFromBill() throws EntityNotFoundException {
        RemoveOrder command = new RemoveOrder(bar.getId(), session.getId(), bill.getId(), order.getId());

        service.handle(command);

        bill = billRepository.findById(bill.getId()).orElseThrow();
        assertTrue(bill.getOrders().isEmpty());
    }

    @Test
    @DisplayName("Delete not existing order from bill")
    void deleteNotExistingOrderFromBill() {
        RemoveOrder command = new RemoveOrder(bar.getId(), session.getId(), bill.getId(), UUID.randomUUID());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.handle(command)
        );
    }
}
