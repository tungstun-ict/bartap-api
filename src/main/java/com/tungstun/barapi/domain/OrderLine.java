package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_line")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private int amount;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    private Product product;

    @OneToOne(
            cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH},
            orphanRemoval = true
    )
    private Bartender bartender;

    public OrderLine() { }
    public OrderLine(Product product, int amount, Bartender bartender) {
        this.product = product;
        this.amount = amount;
        this.bartender = bartender;
        this.creationDate = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Product getProduct() { return product; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public Bartender getBartender() { return bartender; }
}
