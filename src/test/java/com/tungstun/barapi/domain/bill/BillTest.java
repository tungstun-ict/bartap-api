package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.session.Session;
import com.tungstun.barapi.domain.session.SessionFactory;
import com.tungstun.exception.InvalidSessionStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    static Stream<Arguments> provideBills() {
        return Stream.of(
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, List.of()), 0),
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, List.of(
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        1,
                                        null
                                ).create()
                        )),
                        1
                ),
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, List.of(
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        2,
                                        null
                                ).create()
                        )
                        ),
                        2
                ),
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, List.of(
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        1,
                                        null
                                ).create(),
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        2,
                                        null
                                ).create()
                        )),
                        3),
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, List.of(
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        2,
                                        null
                                ).create(),
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        1,
                                        null
                                ).create())),
                        3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBills")
    @DisplayName("Calculate the bills total price")
    void calculateBillTotalPrice(Bill bill, double expectedPrice) {
        double calculatedPrice = bill.calculateTotalPrice();

        assertEquals(expectedPrice, calculatedPrice);
    }

    static Stream<Arguments> provideAddBills() {
        return Stream.of(
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, new ArrayList<>()), 0),
                Arguments.of(new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, new ArrayList<>(Collections.singletonList(
                                new OrderFactory(
                                        new ProductBuilder("", null).setPrice(1.0).build(),
                                        1,
                                        null
                                ).create())
                        ))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideAddBills")
    @DisplayName("Add order to bill")
    void addOrderToBill(Bill bill) {
        Product product = new ProductBuilder("", null).setPrice(1.0).build();
        Person person = new Person();

        assertDoesNotThrow(() -> bill.addOrder(product, 1, person));
    }
    
    static Stream<Arguments> provideIllegalProductArgs() {
        return Stream.of(
                Arguments.of(new ProductBuilder("", null).setPrice(1.0).build(), 1, null),
                Arguments.of(new ProductBuilder("", null).setPrice(1.0).build(), 0, new Person()),
                Arguments.of(new ProductBuilder("", null).setPrice(1.0).build(), -1, new Person()),
                Arguments.of(null, 1, new Person())
        );
    }

    @ParameterizedTest
    @MethodSource("provideIllegalProductArgs")
    @DisplayName("Add product to bill with illegal arguments")
    void addWithIllegalArguments(Product product, int amount, Person bartender) {
        Bill bill = new Bill(UUID.randomUUID(), new SessionFactory("name").create(), false, null, new ArrayList<>());

        assertThrows(
                IllegalArgumentException.class,
                () -> bill.addOrder(product, amount, bartender)
        );
    }

    @Test
    @DisplayName("Remove order from bill")
    void removeOrder() {
        Session session = new SessionFactory("name").create();
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("prod", category).build();
        Order order = bill.addOrder(product, 1, person);

        assertDoesNotThrow(() -> bill.removeOrder(order.getId()));
    }
    
    @Test
    @DisplayName("Remove not existing order from bill returns false")
    void removeNotExistingOrder_ReturnsFalse() {
        Session session = new SessionFactory("name").create();
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);

        assertThrows(
                EntityNotFoundException.class,
                () -> bill.removeOrder(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Remove order from when session has ended throws")
    void removeBill_WhenSessionEnded_Throws() {
        Session session = new SessionFactory("name").create();
        Person person = new PersonBuilder("name").build();
        Bill bill = session.addCustomer(person);
        session.end();

        assertThrows(
                InvalidSessionStateException.class,
                () -> bill.removeOrder(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Add order adds ADD history entry")
    void additionHistoryEntry() {
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("name", category).setBrand("brand").setPrice(2.0).build();
        Session session = new SessionFactory("name").create();
        Person customer = new PersonBuilder("customer").build();
        Person bartender = new PersonBuilder("bartender").build();
        Bill bill = session.addCustomer(customer);
        int amount = 2;
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        bill.addOrder(product, amount, bartender);

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertEquals(1, bill.getHistory().size());
        OrderHistoryEntry additionEntry = bill.getHistory().get(0);
        assertEquals(OrderHistoryType.ADD, additionEntry.getType());
        assertEquals(product.getId(), additionEntry.getProductId());
        assertEquals(product.getId(), additionEntry.getProductId());
        assertEquals(amount, additionEntry.getAmount());
        assertEquals(customer, additionEntry.getCustomer());
        assertEquals(bartender, additionEntry.getBartender());
        assertTrue(additionEntry.getDate().isAfter(before)
                && additionEntry.getDate().isBefore(after));
    }


    @Test
    @DisplayName("Remove order adds REMOVE history entry")
    void removeOrderHistoryEntry() {
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("name", category).setBrand("brand").setPrice(2.0).build();
        Session session = new SessionFactory("name").create();
        Person customer = new PersonBuilder("customer").build();
        Person bartender = new PersonBuilder("bartender").build();
        Bill bill = session.addCustomer(customer);
        int amount = 2;
        Order order = bill.addOrder(product, amount, bartender);
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        bill.removeOrder(order.getId());

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertEquals(2, bill.getHistory().size());
        OrderHistoryEntry removalEntry = bill.getHistory().get(1);
        assertEquals(OrderHistoryType.REMOVE, removalEntry.getType());
        assertEquals(product.getId(), removalEntry.getProductId());
        assertEquals(product.getId(), removalEntry.getProductId());
        assertEquals(amount, removalEntry.getAmount());
        assertEquals(customer, removalEntry.getCustomer());
        assertEquals(bartender, removalEntry.getBartender());
        assertTrue(removalEntry.getDate().isAfter(before)
                && removalEntry.getDate().isBefore(after));
    }
}
