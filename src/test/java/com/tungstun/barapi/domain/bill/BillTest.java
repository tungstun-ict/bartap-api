package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.session.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    @MethodSource("provideBills")
    @DisplayName("Calculate the bills total price")
    void calculateBillTotalPrice(Bill bill, double expectedPrice) {
        double calculatedPrice = bill.calculateTotalPrice();

        assertEquals(expectedPrice, calculatedPrice);
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
    @MethodSource("provideAddBills")
    @DisplayName("Add order to bill")
    void addOrderToBill(Bill bill) {
        Product product = new ProductBuilder("", null).setPrice(1.0).build();
        Person person = new Person();

        assertDoesNotThrow(() -> bill.addOrder(product, 1, person));
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
}