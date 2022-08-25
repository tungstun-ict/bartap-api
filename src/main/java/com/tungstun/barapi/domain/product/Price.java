package com.tungstun.barapi.domain.product;

import com.tungstun.common.money.Money;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "money")
    private Money money;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    protected Price(UUID id, Money money) {
        this.id = id;
        this.money = money;
        this.fromDate = LocalDateTime.now();
    }

    public Price() {
    }

    public static Price create(Money money) {
        return new Price(UUID.randomUUID(), money);
    }

    public boolean isActive() {
        return toDate == null;
    }

    public void endPricing() {
        this.toDate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Money getMoney() {
        return money;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }
}
