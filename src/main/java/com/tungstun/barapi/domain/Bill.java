package com.tungstun.barapi.domain;

import java.util.Date;
import java.util.List;

public class Bill {
    private String id;
    private Date date; // niet nodig door sessie
    private Customer customer;
    private List<Order> orders;

    public Bill(String id, Date date, Customer customer, List<Order> orders) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.orders = orders;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }


    public Customer getCustomer() {
        return customer;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public boolean addBartender(Order order ){
        if ( !this.orders.contains(order) ){
            return this.orders.add(order);
        }
        return false;

    }
    public boolean removeBartender(Order order){
        return this.orders.remove(order);
    }


}
