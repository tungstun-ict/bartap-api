package com.tungstun.barapi.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class ProductRequest {
    @NotNull
    public String name;

    @NotNull
    public String brand;

    @NotNull
    public Double size;

    @NotNull
    public Double price;

    @NotNull
    public Boolean isFavorite;

    @NotNull
    public Long categoryId;

    @NotNull
    public String productType;
}
