package com.tungstun.barapi.domain;

import javax.persistence.*;

@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private int amount;

    @Transient
    private Product product;

    public OrderLine() { }
    public OrderLine(Long id, Product product, int amount) {
        this.id = id;
        this.product = product;
        this.amount = amount;
    }

    public Long getId() { return id; }

    public Product getProduct() { return product; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }
}
