package com.tungstun.barapi.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "drink")
@PrimaryKeyJoinColumn(name = "product_id")
public class Drink extends Product{

    public Drink() { super(); }
    public Drink(String id, String name, String brand, double size, double price, boolean isFavorite, Category category) {
        super(id, name, brand, size, price, isFavorite, category);
    }
}
