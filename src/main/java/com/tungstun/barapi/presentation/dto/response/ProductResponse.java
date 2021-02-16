package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.product.ProductType;

public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private double size;
    private double price;
    private boolean isFavorite;
    private Category category;
    private ProductType productType;

    public ProductResponse() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

    public double getSize() { return size; }

    public void setSize(double size) { this.size = size; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public ProductType getProductType() { return productType; }

    public void setProductType(ProductType productType) { this.productType = productType; }
}
