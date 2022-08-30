package com.tungstun.barapi.port.web.product.response;

import com.tungstun.barapi.domain.product.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response details about the product")
public class ProductResponse {

    @ApiModelProperty(notes = "The product's categoryId")
    private UUID id;

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

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

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
