package com.tungstun.barapi.domain;

public class Drink extends Product{

    public Drink(String id, String name, String brand, double size, double price, boolean isFavorite, Category category) {
        super(id, name, brand, size, price, isFavorite, category);
    }
}
