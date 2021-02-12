package com.tungstun.barapi.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "food")
@PrimaryKeyJoinColumn(name = "product_id")
public class Food extends Product{

    public Food() { super(); }
    public Food(String id, String name, String brand, double size, double price, boolean isFavorite, Category category) {
        super(id, name, brand, size, price, isFavorite, category);
    }
}
