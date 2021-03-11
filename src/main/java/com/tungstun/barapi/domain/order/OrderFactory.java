package com.tungstun.barapi.domain.order;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;

public class OrderFactory {
    private Person bartender;
    private int amount;
    private Product product;

    public OrderFactory(Product product, Person bartender, int amount) {
        this.bartender = bartender;
        this.amount = amount;
        this.product = product;
    }

    public Order create(){
        return new Order(
                this.product,
                this.amount,
                this.bartender
        );
    }

}
