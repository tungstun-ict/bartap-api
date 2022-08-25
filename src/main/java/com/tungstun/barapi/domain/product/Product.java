package com.tungstun.barapi.domain.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.common.money.Money;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "product")
@Where(clause = "deleted = false")
public class Product {
    @Column(name = "deleted")
    private final boolean deleted = Boolean.FALSE;

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "size")
    private double size;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_price",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "price_id")
    )
    private List<Price> prices;

    @ManyToOne
    private Category category;

    public Product() {
    }

    public Product(UUID id, String name, String brand, double size, boolean isFavorite, ProductType type, Money price, Category category) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.isFavorite = isFavorite;
        this.type = type;
        this.prices = new ArrayList<>(List.of(Price.create(price)));
        this.category = category;
    }


    public Money getPriceAtDate(LocalDateTime localDateTime) {
        return prices.stream()
                .filter(price -> localDateTime.isAfter(price.getFromDate()))
                .filter(price -> price.getToDate() == null || localDateTime.isBefore(price.getToDate()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Product didn't exist before %s", localDateTime)))
                .getMoney();
    }

    public Money getPrice() {
        return getCurrentPrice().getMoney();
    }

    private Price getCurrentPrice() {
        return prices.stream()
                .filter(Price::isActive)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product doesn't have a current price"));
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null) throw new IllegalArgumentException("Monetary value cannot be null");
        getCurrentPrice().endPricing();
        prices.add(Price.create(newPrice));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return deleted == product.deleted
                && Double.compare(product.size, size) == 0
                && isFavorite == product.isFavorite
                && Objects.equals(id, product.id)
                && Objects.equals(name, product.name)
                && Objects.equals(brand, product.brand)
                && type == product.type
                && Objects.equals(prices, product.prices)
                && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted, id, name, brand, size, isFavorite, type, prices, category);
    }
}
