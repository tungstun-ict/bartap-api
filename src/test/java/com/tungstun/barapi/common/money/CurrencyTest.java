package com.tungstun.barapi.common.money;

import com.tungstun.common.money.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrencyTest {
    @Test
    void compareSameCodes_ReturnsZero(){
        Currency currency = new Currency("", "EUR");
        Currency currency2 = new Currency("", "EUR");
        assertEquals(0, currency.compareTo(currency2));
    }

    @Test
    void compareEarlierCodeToLaterCode_ReturnsOne (){
        Currency currency = new Currency("", "EUR");
        Currency currency2 = new Currency("", "USD");
        assertTrue(currency.compareTo(currency2) < 0);
    }

    @Test
    void compareLaterCodeToCode_ReturnsMinusOne (){
        Currency currency = new Currency("", "USD");
        Currency currency2 = new Currency("", "EUR");
        assertTrue(currency.compareTo(currency2) > 0);
    }

}