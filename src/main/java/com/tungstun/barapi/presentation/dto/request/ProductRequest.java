package com.tungstun.barapi.presentation.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Request details about the product")
public class ProductRequest {
    @ApiModelProperty(notes = "The product's name")
    @NotNull
    public String name;

    @ApiModelProperty(notes = "The product's brand")
    @NotNull
    public String brand;

    @ApiModelProperty(notes = "The product's size")
    @NotNull
    public Double size;

    @ApiModelProperty(notes = "The product's price")
    @NotNull
    public Double price;

    @ApiModelProperty(notes = "Is product favorite")
    @NotNull
    public Boolean isFavorite;

    @ApiModelProperty(notes = "The ID of the product's category")
    @NotNull
    public Long categoryId;

    @ApiModelProperty(notes = "The product's type")
    @NotNull
    public String productType;
}
