package com.tungstun.barapi.domain;

import java.util.Date;

public class Order {
    private String id;
    private Date date;
    private int amount;
    private double price;
    private Bartender bartender;
    private Product product;

    public Order(String id, Date date, int amount, double price, Bartender bartender, Product product) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.price = price;
        this.bartender = bartender;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public Bartender getBartender() {
        return bartender;
    }

    public Product getProduct() {
        return product;
    }
}
