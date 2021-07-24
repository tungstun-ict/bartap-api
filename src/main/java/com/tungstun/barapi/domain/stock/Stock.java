package com.tungstun.barapi.domain.stock;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private long amount;

    public Stock() { }
    public Stock(long amount) {
        this.amount = amount;
    }

    public boolean increaseAmount(long amount) {
        if (!isValidAmount(amount)) return false;
        this.amount += amount;
        return true;
    }

    public boolean decreaseAmount(long amount) {
        if (!isValidAmount(amount)) return false;
        this.amount -= amount;
        return true;
    }

    private boolean isValidAmount (long amount) {
        return amount > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
