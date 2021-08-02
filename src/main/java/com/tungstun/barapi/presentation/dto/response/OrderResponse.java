package com.tungstun.barapi.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.domain.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the order")
public class OrderResponse {

    @ApiModelProperty(notes = "The order's id")
    private Long id;

    @ApiModelProperty(notes = "The order's amount of products")
    private int amount;

    @ApiModelProperty(notes = "The order's creation date")
    private LocalDateTime creationDate;

    @ApiModelProperty(notes = "The order's product")
    private Product product;

    @ApiModelProperty(notes = "The order's bartender")
    private PersonResponse bartender;

    @ApiModelProperty(notes = "The order's customer")
    private PersonResponse customer;

    public OrderResponse() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public PersonResponse getBartender() { return bartender; }

    public void setBartender(PersonResponse bartender) { this.bartender = bartender; }

    public PersonResponse getCustomer() {
        return customer;
    }

    public void setCustomer(PersonResponse customer) {
        this.customer = customer;
    }
}
