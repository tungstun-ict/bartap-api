package com.tungstun.barapi.domain.session.state;

import com.tungstun.barapi.domain.person.Person;

import java.util.UUID;

public interface SessionState {
    void end();

    void lock();

    void addCustomer(Person person);

    void payBill(UUID id);

    void removeBill(UUID id);


}
