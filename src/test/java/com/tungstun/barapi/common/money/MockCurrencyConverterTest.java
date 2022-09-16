package com.tungstun.barapi.common.money;

import com.tungstun.common.money.Currency;
import com.tungstun.common.money.MockCurrencyConverter;
import com.tungstun.common.money.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MockCurrencyConverterTest {

    @Test
    void mockConvertor_ReturnsSameAmountInDifferentCurrency() {
        Money money = new Money(BigDecimal.valueOf(15.65), new Currency("$", "USD"));
        Currency newCurrency = new Currency("€", "EUR");

        Money convertedMoney = new MockCurrencyConverter().convert(money, newCurrency);

        assertEquals(money.amount(), convertedMoney.amount());
        assertNotEquals(money.currency(), convertedMoney.currency());
    }
}