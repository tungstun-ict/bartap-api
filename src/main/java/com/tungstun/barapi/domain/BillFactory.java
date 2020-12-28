package com.tungstun.barapi.domain;

import java.util.ArrayList;

public class BillFactory {
    private Session session;
    private Customer customer;

    public BillFactory(Session session, Customer customer) {
        this.session = session;
        this.customer = customer;
    }

    public Bill create(){
        return new Bill(session, customer, new ArrayList<>(), false);
    }
}
