package com.tungstun.barapi.domain;

public class Food extends Product{
    public Food(String id, String name, String brand, double size, double price, boolean isFavorite, Category category) {
        super(id, name, brand, size, price, isFavorite, category);
    }
}
