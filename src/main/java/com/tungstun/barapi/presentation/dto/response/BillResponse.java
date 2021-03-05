package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.order.Order;

import java.util.List;

public class BillResponse {
    private String id;
    private boolean isPayed;
    private Customer customer;
    private Double totalPrice;
    private List<Order> orders;
    private Session session;

    public BillResponse() { }

    public BillResponse(String id,
                        boolean isPayed,
                        Customer customer,
                        Double totalPrice,
                        List<Order> orders,
                        Session session)
    {
        this.id = id;
        this.isPayed = isPayed;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.orders = orders;
        this.session = session;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public Double getTotalPrice() { return totalPrice; }

    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public List<Order> getOrders() { return orders; }

    public void setOrders(List<Order> orders) { this.orders = orders; }

    public Session getSession() { return session; }

    public void setSession(Session session) { this.session = session; }
}
