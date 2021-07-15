package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.product.ProductType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response details about the category")
public class CategoryResponse {
    @ApiModelProperty(notes = "The category's id")
    private Long id;

    @ApiModelProperty(notes = "The category's name")
    private String name;

    @ApiModelProperty(notes = "The category's product type")
    private ProductType productType;

    public CategoryResponse() { }
    public CategoryResponse(Long id, String name, ProductType productType) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public ProductType getProductType() { return productType; }

    public void setProductType(ProductType productType) { this.productType = productType; }
}
