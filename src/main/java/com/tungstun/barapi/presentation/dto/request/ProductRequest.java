package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details about the product")
public class ProductRequest {
    @ApiModelProperty(notes = "The product's name")
    @NotBlank(message = "Name cannot be null or blank")
    public String name;

    @ApiModelProperty(notes = "The product's brand")
    @NotBlank(message = "Brand cannot be null or blank")
    public String brand;

    @ApiModelProperty(notes = "The product's size")
    @Min(value = 0, message = "Size cannot be 0 or lower")
    public Double size;

    @ApiModelProperty(notes = "The product's price")
    @Min(value = 0, message = "Price cannot be 0 or lower")
    public Double price;

    @ApiModelProperty(notes = "Is product favorite")
    @NotNull
    public Boolean isFavorite;

    @ApiModelProperty(notes = "The product's type")
    @NotNull
    public String productType;

    @ApiModelProperty(notes = "The ID of the product's category")
    @NotNull
    public UUID categoryId;
}
