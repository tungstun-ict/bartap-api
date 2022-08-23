package com.tungstun.barapi.domain.payment;

import com.tungstun.common.money.Money;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderProduct {
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Embedded
    private Money price;

    public OrderProduct() {

    }

    public OrderProduct(Long id, String name, String brand, Money price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public Long getId() {
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
}
