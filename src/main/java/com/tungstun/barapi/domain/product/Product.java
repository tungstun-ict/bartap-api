package com.tungstun.barapi.domain.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.domain.stock.Stock;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "product")
@Where(clause = "deleted = false")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "deleted")
    private final boolean deleted = Boolean.FALSE;

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

    @ManyToOne
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    private Stock stock;

    public Product() { }
    public Product(String name, String brand, double size, double price, boolean isFavorite, Category category, Stock stock) {
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.isFavorite = isFavorite;
        this.category = category;
        this.stock = stock;
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

    public Stock getStock() {
        return stock;
    }

    public void setName(String name) { this.name = name; }

    public void setBrand(String brand) { this.brand = brand; }

    public void setSize(double size) { this.size = size; }

    public void setPrice(double price) { this.price = price; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public void setCategory(Category category) { this.category = category; }
}
