package com.tungstun.barapi.domain.product;

import com.tungstun.common.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {
    @Test
    @DisplayName("Price private factory creates price at LocalDateTime now")
    void constructPrice_CreatesPricesWithMoneyAndCurrentDate() {
        Price price = Price.create(new Money(2.5d));

        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime priceFromDate = price.getFromDate().truncatedTo(ChronoUnit.SECONDS);
        assertTrue(priceFromDate.isEqual(localDateTime));
        assertNull(price.getToDate());
    }

    @Test
    @DisplayName("Created price is active")
    void newPrice_IsActive() {
        Price price = Price.create(new Money(2.5d));

        assertTrue(price.isActive());
    }

    @Test
    @DisplayName("Ended price is not active")
    void endPrice_MakesPriceNotActive() {
        Price price = Price.create(new Money(2.5d));
        price.endPricing();

        assertFalse(price.isActive());
        assertNotNull(price.getToDate());
    }
}