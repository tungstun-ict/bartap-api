package com.tungstun.barapi.domain.payment;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;

import java.util.UUID;

public class OrderFactory {
    private final Product product;
    private final Integer amount;
    private final Person bartender;

    public OrderFactory(Product product, Integer amount, Person bartender) {
        this.product = product;
        this.amount = amount;
        this.bartender = bartender;
    }

    public Order create() {
        return new Order(
                UUID.randomUUID(),
                new OrderProduct(product.getId(), product.getName(), product.getBrand(), product.getPrice()),
                amount,
                bartender
        );
    }
}
