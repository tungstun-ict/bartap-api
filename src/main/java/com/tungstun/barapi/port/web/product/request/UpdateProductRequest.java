package com.tungstun.barapi.port.web.product.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel(description = "Request details to update a product")
public record UpdateProductRequest(
    @ApiModelProperty(notes = "The product's name")
    @NotBlank(message = "Name cannot be null or blank")
    String name,

    @ApiModelProperty(notes = "The product's brand")
    @NotBlank(message = "Brand cannot be null or blank")
    String brand,

    @ApiModelProperty(notes = "The product's size")
    @Min(value = 0, message = "Size cannot be 0 or lower")
    Double size,

    @ApiModelProperty(notes = "The product's price")
    @Min(value = 0, message = "Price cannot be 0 or lower")
    Double price,

    @ApiModelProperty(notes = "Is product favorite")
    @NotNull
    Boolean isFavorite,

    @ApiModelProperty(notes = "The product's type")
    @NotNull
    String productType,

    @ApiModelProperty(notes = "The ID of the product's category")
    @NotNull
    UUID categoryId) {
}
