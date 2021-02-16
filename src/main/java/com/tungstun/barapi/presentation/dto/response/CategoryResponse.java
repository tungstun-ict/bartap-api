package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.product.ProductType;

public class CategoryResponse {
    private Long id;
    private String name;
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
