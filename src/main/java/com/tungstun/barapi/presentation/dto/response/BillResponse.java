package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

@ApiModel(description = "Response details about the bill")
public class BillResponse {
    @ApiModelProperty(notes = "The bill's categoryId")
    private UUID id;

    @ApiModelProperty(notes = "The bill's payment state")
    private boolean isPayed;

    @ApiModelProperty(notes = "The bill's customer")
    private Person customer;

    @ApiModelProperty(notes = "The bill's total price")
    private Double totalPrice;

    @ApiModelProperty(notes = "The bill's orders")
    private List<OrderResponse> orders;

    @ApiModelProperty(notes = "The bill's affiliated session")
    private Session session;

    public BillResponse() { }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public Person getCustomer() { return customer; }

    public void setCustomer(Person customer) { this.customer = customer; }

    public Double getTotalPrice() { return totalPrice; }

    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public List<OrderResponse> getOrders() { return orders; }

    public void setOrders(List<OrderResponse> orders) { this.orders = orders; }

    public Session getSession() { return session; }

    public void setSession(Session session) { this.session = session; }
}
