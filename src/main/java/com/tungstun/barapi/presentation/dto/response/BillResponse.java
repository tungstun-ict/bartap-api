package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Order;
import com.tungstun.barapi.domain.Session;

import java.util.List;

public class BillResponse {
    private String id;
    private boolean isPayed;
    private Customer customer;
    private List<Order> orders;
    private Session session;

    public BillResponse() { }
    public BillResponse(String id, boolean isPayed, Customer customer, List<Order> orders) {
        this.id = id;
        this.isPayed = isPayed;
        this.customer = customer;
        this.orders = orders;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Order> getOrders() { return orders; }

    public boolean addOrder(Order order){
        if ( !this.orders.contains(order) ) return this.orders.add(order);
        return false;
    }

    public boolean removeOrder(Order order){ return this.orders.remove(order); }

    public Session getSession() { return session; }

    public void setSession(Session session) { this.session = session; }
}
