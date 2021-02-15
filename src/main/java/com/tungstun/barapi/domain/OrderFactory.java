package com.tungstun.barapi.domain;

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
