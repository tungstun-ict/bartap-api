package com.tungstun.barapi.domain.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTest {
    private Stock stock;

    private static Stream<Arguments> validValues() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(999999999999999999L)
        );
    }

    private static Stream<Arguments> invalidValues() {
        return Stream.of(
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
        long expectedAmount = stock.getAmount() + amount;
        stock.increaseAmount(amount);

        assertEquals(expectedAmount, stock.getAmount());
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    @DisplayName("Increase stock invalid")
    void increaseStockInvalid(long amount) {
        assertThrows(
                IllegalArgumentException.class,
                () -> stock.increaseAmount(amount)
        );
    }

    @ParameterizedTest
    @MethodSource("validValues")
    @DisplayName("Decrease stock valid")
    void decreaseStockValid(long amount) {
        long expectedAmount = stock.getAmount() - amount;
        stock.decreaseAmount(amount);

        assertEquals(expectedAmount, stock.getAmount());
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    @DisplayName("Decrease stock invalid")
    void decreaseStockInvalid(long amount) {
        assertThrows(
                IllegalArgumentException.class,
                () -> stock.decreaseAmount(amount)
        );
    }
}