package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "date")
    private Date date;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private double price;

    @Transient
    private Bartender bartender;

    @Transient
    private Product product;

    public Order() { }
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
