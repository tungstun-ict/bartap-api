package com.tungstun.barapi.domain.payment;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    static Stream<Arguments> provideBills() {
        return Stream.of(
                Arguments.of(new Bill(null, null, List.of(), false), 0),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                1,
                                null
                        )
                        ), false),
                        1
                ),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                2,
                                null
                        )
                        ), false),
                        2
                ),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                1,
                                null
                        ),
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                1,
                                null
                        )
                        ), false),
                        2),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                2,
                                null
                        ),
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                1,
                                null
                        )
                        ), false),
                        3)
        );
    }

    static Stream<Arguments> provideAddBills() {
        return Stream.of(
                Arguments.of(new Bill(null, null, new ArrayList<>(), false)),
                Arguments.of(new Bill(null, null, new ArrayList<>(Collections.singletonList(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null, null),
                                1,
                                null
                        )
                        )), false)
                )
        );
    }

    static Stream<Arguments> provideIllegalProductArgs() {
        return Stream.of(
                Arguments.of(new Product(null, null, 0, 1.0, false, null, null), 1, null),
                Arguments.of(new Product(null, null, 0, 1.0, false, null, null), 0, new Person()),
                Arguments.of(new Product(null, null, 0, 1.0, false, null, null), -1, new Person()),
                Arguments.of(null, 1, new Person())
        );
    }

    @ParameterizedTest
    @MethodSource("provideBills")
    @DisplayName("Calculate the bills total price")
    void calculateBillTotalPrice(Bill bill, double expectedPrice) {
        double calculatedPrice = bill.calculateTotalPrice();

        assertEquals(expectedPrice, calculatedPrice);
    }

    @ParameterizedTest
    @MethodSource("provideAddBills")
    @DisplayName("Add order to bill")
    void addOrderToBill(Bill bill) {
        Product product = new Product(null, null, 0, 1.0, false, null, null);
        Person person = new Person();

        boolean isSuccessful = bill.addOrder(product, 1, person);

        assertTrue(isSuccessful);
    }

    @ParameterizedTest
    @MethodSource("provideIllegalProductArgs")
    @DisplayName("Add product to bill with illegal arguments")
    void addWithIllegalArguments() {
        Bill bill = new Bill(null, null, new ArrayList<>(), false);
        Product product = new Product(null, null, 0, 1.0, false, null, null);

        boolean isSuccessful = bill.addOrder(product, 1, null);

        assertFalse(isSuccessful);
    }
}