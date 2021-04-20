package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.order.Order;
import com.tungstun.barapi.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class BillTest {
    static Stream<Arguments> provideBills () {
        return Stream.of(
                Arguments.of(new Bill(null, null, List.of(), false), 1),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                1,
                                null
                        )
                ), false), 1),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                2,
                                null
                        )
                ), false), 2),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                1,
                                null
                        ),
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                1,
                                null
                        )
                ), false), 2),
                Arguments.of(new Bill(null, null, List.of(
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                2,
                                null
                        ),
                        new Order(
                                new Product(null, null, 0, 1.0, false, null),
                                1,
                                null
                        )
                ), false), 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBills")
    @DisplayName("Calculate the bills total price")
    void calculateBillTotalPrice(Bill bill, double expectedPrice) {

    }
}