package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.product.ProductType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response details about the product")
public class ProductResponse {

    @ApiModelProperty(notes = "The product's id")
    private Long id;

    @ApiModelProperty(notes = "The product's name")
    private String name;

    @ApiModelProperty(notes = "The product's brand")
    private String brand;

    @ApiModelProperty(notes = "The product's size")
    private double size;

    @ApiModelProperty(notes = "The product's price")
    private double price;

    @ApiModelProperty(notes = "Is product favorited")
    private boolean isFavorite;

    @ApiModelProperty(notes = "The product's category")
    private Category category;

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
}
