package com.tungstun.barapi.domain.product;

import com.tungstun.common.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.util.FieldUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {
    private static final double PRICE = 2.5;
    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new CategoryFactory("category").create();
        product = new ProductBuilder("product", category)
                .setPrice(PRICE)
                .build();
    }

    @Test
    @DisplayName("Get current price returns currently newest price")
    void currentPrice_ReturnsTheActivePrice() {
        assertEquals(PRICE, product.getPrice().amount().doubleValue());
    }

    @Test
    @DisplayName("Update price with null value throws")
    void updatePriceIsNull_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> product.updatePrice(null)
        );
    }


    @Test
    @DisplayName("Update price sets new price as current price")
    void updatePrice_ChangesCurrentPrice() {
        assertEquals(PRICE, product.getPrice().amount().doubleValue());
        Money newPrice = new Money(2.0d);

        product.updatePrice(newPrice);

        assertEquals(newPrice, product.getPrice());
    }

    @Test
    @DisplayName("Update price multiple times sets newest updated value as current price")
    void currentPriceWithMultiplePrices_ReturnsTheNewestActivePrice() {
        Money price2 = new Money(3d);
        Money price3 = new Money(4d);
        product.updatePrice(price2);
        product.updatePrice(price3);

        Money currentPrice = product.getPrice();

        assertEquals(price3, currentPrice);
    }

    @Test
    @DisplayName("Get current price when none exist throws IllegalArgumentException (Should not be possible)")
    void noCurrentPrice_Throws() {
        List<Price> allPrices = (List<Price>) FieldUtils.getProtectedFieldValue("prices", product);
        allPrices.get(0).endPricing();

        assertThrows(
                IllegalArgumentException.class,
                product::getPrice
        );
    }

    @Test
    @DisplayName("Get price at date with one price returns current price")
    void priceAtDateWithOnePrice_EqualsCurrent() {
        Money currentPrice = product.getPrice();
        Money atDatePrice = product.getPriceAtDate(LocalDateTime.now().plusSeconds(5));

        assertEquals(currentPrice, atDatePrice);
    }

    @Test
    @DisplayName("Get price at date after last update after multiple price updates returns current price")
    void priceAtDateAfterNowWithMultiplePrices_EqualsCurrent() {
        Money money2 = new Money(3d);
        Money money3 = new Money(4d);
        product.updatePrice(money2);
        product.updatePrice(money3);

        Money atDatePrice = product.getPriceAtDate(LocalDateTime.now().plusSeconds(1));

        assertEquals(money3, atDatePrice);
    }

    @SuppressWarnings("java:S2925")
    @Test
    @DisplayName("Get price at date after second to last update after multiple price updates returns second to last price")
    void priceAtPreviousDateWithMultiplePrices_EqualsCurrent() throws InterruptedException {
        Thread.sleep(10); // Sleep thread to make sure localDateTime is after creation date of first price
        LocalDateTime localDateTime = LocalDateTime.now();
        Thread.sleep(10); // Sleep thread to make sure localDateTime is before creation date of second price
        Money money2 = new Money(3d);
        product.updatePrice(money2);

        Money atDatePrice = product.getPriceAtDate(localDateTime);

        assertEquals(PRICE, atDatePrice.amount().doubleValue());
    }

    @Test
    @DisplayName("Get price at date before initial price throws IllegalArgumentException")
    void priceAtDateBeforeInitialPriceDate_Throws() {
        LocalDateTime localDateTime = LocalDateTime.now().minusSeconds(5);

        assertThrows(
                IllegalArgumentException.class,
                () -> product.getPriceAtDate(localDateTime)
        );
    }
}