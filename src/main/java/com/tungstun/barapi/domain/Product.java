package com.tungstun.barapi.domain;

public class Product {
    private String id;
    private String name;
    private String brand;
    private double size;
    private double price;
    private boolean isFavorite;
    private Category category;

    public Product(String id, String name, String brand, double size, double price, boolean isFavorite, Category category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.isFavorite = isFavorite;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Category getCategory() {
        return category;
    }
}
