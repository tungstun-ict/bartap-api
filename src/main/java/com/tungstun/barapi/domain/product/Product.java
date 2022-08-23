package com.tungstun.barapi.domain.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.common.money.Money;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "product")
@Where(clause = "deleted = false")
public class Product {
    @Column(name = "deleted")
    private final boolean deleted = Boolean.FALSE;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bar_id")
    private Long barId;

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

    @Embedded
    private Prices prices;

    @ManyToOne
    private Category category;

    public Product() { }

    public Product(Long barId, String name, String brand, double size, boolean isFavorite, ProductType type, Money price, Category category) {
        this.barId = barId;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.isFavorite = isFavorite;
        this.type = type;
        this.prices = new Prices(price);
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public Long getBarId() {
        return barId;
    }

    public void setBarId(Long barId) {
        this.barId = barId;
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

    public Money getPrice() {
        return prices.currentPrice().getMoney();
    }

    public Money getPriceAtDate(LocalDateTime date) {
        return prices.atDate(date).getMoney();
    }

    public void updatePrice(Money price) {
        this.prices.updatePrice(price);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
