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

    public void increaseAmount(long amount) {
        validateAmount(amount);
        this.amount += amount;
    }

    public void decreaseAmount(long amount) {
        validateAmount(amount);
        this.amount -= amount;
    }

    private void validateAmount(long amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount not allowed to be smaller than 0");
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
