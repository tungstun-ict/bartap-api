package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryFactory;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderHistoryTest {
    @Test
    void addHistoryEntry() {
        Person customer = new PersonBuilder("customer").build();
        Person bartender = new PersonBuilder("bartender").build();
        Category category = new CategoryFactory("category").create();
        Product product = new ProductBuilder("name", category).setBrand("brand").setPrice(2.0).build();
        int amount = 2;
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        Order order = new OrderFactory(product, amount, bartender).create();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        OrderHistory history = new OrderHistory(new ArrayList<>());

        history.addEntry(order, customer);

        assertEquals(1, history.getHistory().size());
        OrderHistoryEntry entry = history.getHistory().get(0);
        assertEquals(customer, entry.getCustomer());
        assertEquals(bartender, entry.getBartender());
        assertEquals(amount, entry.getAmount());
        assertTrue(entry.getDate().isAfter(before)
                && entry.getDate().isBefore(after));
    }
}