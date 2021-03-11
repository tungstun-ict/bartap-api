package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.Session;
import com.tungstun.barapi.domain.person.Person;

import java.util.ArrayList;

public class BillFactory {
    private Session session;
    private Person customer;

    public BillFactory(Session session, Person customer) {
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
