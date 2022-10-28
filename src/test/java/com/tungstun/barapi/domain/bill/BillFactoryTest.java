package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.session.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillFactoryTest {
    @Test
    @DisplayName("Create new Bill with Session and Customer")
    void createNewBill() {
        Session session = new Session();
        Person customer = new Person();

        Bill bill = new BillFactory(session, customer).create();

        assertEquals(customer, bill.getCustomer());
        assertTrue(bill.getOrders().isEmpty());
        assertFalse(bill.isPayed());
    }
}