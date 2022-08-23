package com.tungstun.barapi.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tungstun.barapi.domain.payment.OrderProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the order")
public class OrderResponse {
    @ApiModelProperty(notes = "The order's id")
    private UUID id;

    @ApiModelProperty(notes = "The order's amount of products")
    private int amount;

    @ApiModelProperty(notes = "The order's creation date")
    private LocalDateTime creationDate;

    @ApiModelProperty(notes = "The order's product")
    private OrderProduct product;

    @ApiModelProperty(notes = "The order's bartender")
    private PersonResponse bartender;

    public OrderResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OrderProduct getProduct() {
        return product;
    }

    public void setProduct(OrderProduct product) {
        this.product = product;
    }

    public PersonResponse getBartender() {
        return bartender;
    }

    public void setBartender(PersonResponse bartender) {
        this.bartender = bartender;
    }
}
