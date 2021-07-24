package com.tungstun.barapi.domain.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockTest {
    private Stock stock;

    private static Stream<Arguments> validValues() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(999999999999999999L)
        );
    }

    private static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(-1),
                Arguments.of(-999999999999999999L)
        );
    }

    @BeforeEach
    void setUp() {
        stock = new Stock(0);
    }

    @ParameterizedTest
    @MethodSource("validValues")
    @DisplayName("Increase stock valid")
    void increaseStockValid(long amount) {
        boolean succeeded = stock.increaseAmount(amount);

        assertTrue(succeeded);
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    @DisplayName("Increase stock invalid")
    void increaseStockInvalid(long amount) {
        boolean succeeded = stock.increaseAmount(amount);

        assertFalse(succeeded);
    }

    @ParameterizedTest
    @MethodSource("validValues")
    @DisplayName("Decrease stock valid")
    void decreaseStockValid(long amount) {
        boolean succeeded = stock.decreaseAmount(amount);

        assertTrue(succeeded);
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    @DisplayName("Decrease stock invalid")
    void decreaseStockInvalid(long amount) {
        boolean succeeded = stock.decreaseAmount(amount);

        assertFalse(succeeded);
    }
}