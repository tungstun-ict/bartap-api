package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.Customer;
import com.tungstun.barapi.domain.Session;

import java.util.ArrayList;

public class BillFactory {
    private Session session;
    private Customer customer;

    public BillFactory(Session session, Customer customer) {
        this.session = session;
        this.customer = customer;
    }

    public Bill create(){
        return new Bill(
                session,
                customer,
                new ArrayList<>(),
                false);
    }
}
