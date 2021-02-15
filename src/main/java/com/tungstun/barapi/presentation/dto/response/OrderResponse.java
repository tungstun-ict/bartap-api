package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Product;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private int amount;
    private LocalDateTime creationDate;
    private Product product;
    private Bartender bartender;

    public OrderResponse() { }

    public OrderResponse(Long id, int amount, LocalDateTime creationDate, Product product, Bartender bartender) {
        this.id = id;
        this.amount = amount;
        this.creationDate = creationDate;
        this.product = product;
        this.bartender = bartender;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Bartender getBartender() { return bartender; }

    public void setBartender(Bartender bartender) { this.bartender = bartender; }
}
