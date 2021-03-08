package com.tungstun.barapi.domain.order;

import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.product.Product;

public class OrderFactory {
    private Bartender bartender;
    private int amount;
    private Product product;

    public OrderFactory(Product product, Bartender bartender, int amount) {
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
