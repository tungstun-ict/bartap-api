package com.tungstun.barapi.domain.bill;

import com.tungstun.common.money.Money;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class OrderProduct {
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Embedded
    private Money price;

    public OrderProduct() {

    }

    public OrderProduct(UUID id, String name, String brand, Money price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
