package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

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
    private Person bartender;


    public OrderResponse() { }
    public OrderResponse(Long id, int amount, LocalDateTime creationDate, Product product, Person bartender) {
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

    public Person getBartender() { return bartender; }

    public void setBartender(Person bartender) { this.bartender = bartender; }
}
