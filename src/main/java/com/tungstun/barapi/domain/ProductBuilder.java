package com.tungstun.barapi.domain;

public class ProductBuilder {
    private String name;
    private String brand;
    private double size;
    private double price;
    private boolean isFavorite;
    private Category category;
    private ProductType productType;

    public ProductBuilder() {
        this.name = "Unknown";
        this.brand = "Unknown";
        this.size = 0;
        this.price = 0.0;
        this.isFavorite = false;
        this.category = null;
        this.productType = ProductType.OTHER;
    }

    public ProductBuilder setName(String name) {
        if (name != null) this.name = name;
        return this;
    }

    public ProductBuilder setBrand(String brand) {
        if (brand != null) this.brand = brand;
        return this;
    }

    public ProductBuilder setSize(Double size) {
        if (size != null) this.size = size;
        return this;
    }

    public ProductBuilder setPrice(Double price) {
        if (price != null) this.price = price;
        return this;
    }

    public ProductBuilder setFavorite(Boolean favorite) {
        if (favorite != null) isFavorite = favorite;
        return this;
    }

    public ProductBuilder setCategory(Category category) {
        if (category != null) this.category = category;
        return this;
    }

    public ProductBuilder setProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public Product build(){
        return new Product(
                this.name,
                this.brand,
                this.size,
                this.price,
                this.isFavorite,
                this.category,
                this.productType
        );
    }
}
