package com.tungstun.barapi.domain.payment;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;

import java.util.ArrayList;
import java.util.UUID;

public class BillFactory {
    private final Session session;
    private final Person customer;

    public BillFactory(Session session, Person customer) {
        this.session = session;
        this.customer = customer;
    }

    public Bill create() {
        return new Bill(
                UUID.randomUUID(),
                session,
                false,
                customer,
                new ArrayList<>()
        );
    }
}
