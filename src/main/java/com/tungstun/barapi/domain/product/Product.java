package com.tungstun.barapi.domain.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.domain.Category;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "size")
    private double size;

    @Column(name = "price")
    private double price;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    public Product() { }
    public Product(String name, String brand, double size, double price, boolean isFavorite, Category category) {
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.isFavorite = isFavorite;
        this.category = category;
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


    public void setName(String name) { this.name = name; }

    public void setBrand(String brand) { this.brand = brand; }

    public void setSize(double size) { this.size = size; }

    public void setPrice(double price) { this.price = price; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public void setCategory(Category category) { this.category = category; }
}
