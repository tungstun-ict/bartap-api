package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response details about the category")
public class CategoryResponse {
    @ApiModelProperty(notes = "The category's id")
    private Long id;

    @ApiModelProperty(notes = "The category's name")
    private String name;

    @ApiModelProperty(notes = "The category's product type")
    private String productType;

    public CategoryResponse() { }
    public CategoryResponse(Long id, String name, String productType) {
        this.id = id;
        this.name = name;
        this.productType = productType;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getProductType() { return productType; }

    public void setProductType(String productType) { this.productType = productType; }
}
