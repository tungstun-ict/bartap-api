package com.tungstun.barapi.domain.product;

import java.util.UUID;

public class CategoryFactory {
    private String name;

    public CategoryFactory(String name) {
        this.name = name;
    }

    public Category create() {
        return new Category(UUID.randomUUID(), name);
    }
}
