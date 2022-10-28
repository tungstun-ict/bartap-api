package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class OrderTest {
    private static final Long BAR_ID = 123L;
    private static final Person BARTENDER = new PersonBuilder("bartender").build();

    private static Stream<Arguments> orderPricesTestArgs() {
        Category category = new CategoryFactory("category").create();
        return Stream.of(
                Arguments.of(
                        2.0d,
                        1,
                        new ProductBuilder("name", category).setBrand("brand").setPrice(2.0).build()
                ),
                Arguments.of(
                        4.0d,
                        2,
                        new ProductBuilder("name", category).setBrand("brand").setPrice(2.0).build()
                ),
                Arguments.of(
                        0.0d,
                        1,
                        new ProductBuilder("name", category).setBrand("brand").setPrice(0.0).build()
                ),
                Arguments.of(
                        0.0d,
                        2,
                        new ProductBuilder("name", category).setBrand("brand").setPrice(0.0).build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("orderPricesTestArgs")
    void orderPrice(Double expectedValue, Integer amountOfProducts, Product product) {
        Order order = new OrderFactory(product, amountOfProducts, BARTENDER).create();

        assertEquals(expectedValue, order.orderPrice());
    }
}