package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Product;

public class OrderLineResponse {
    private Long id;
    private Product product;
    private int amount;

    public OrderLineResponse() { }
    public OrderLineResponse(Long id, Product product, int amount) {
        this.id = id;
        this.product = product;
        this.amount = amount;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }
}
