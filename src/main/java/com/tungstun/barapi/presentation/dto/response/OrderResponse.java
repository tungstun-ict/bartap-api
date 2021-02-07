package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.OrderLine;

import java.util.Date;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Date date;
    private double price;
    private Bartender bartender;
    private List<OrderLine> orderLines;

    public OrderResponse() { }
    public OrderResponse(Long id, Date date, double price, Bartender bartender, List<OrderLine> orderLines) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.bartender = bartender;
        this.orderLines = orderLines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Bartender getBartender() {
        return bartender;
    }

    public void setBartender(Bartender bartender) {
        this.bartender = bartender;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
