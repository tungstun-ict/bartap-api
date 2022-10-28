package com.tungstun.statistics.domain.statistics.filter;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.barapi.domain.bill.BillFactory;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.security.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BillFromUserWithUsernameFilterTest {
    private User user;

    @BeforeEach
    void init() {
        user = new User(
                UUID.randomUUID(),
                "username",
                "password",
                "mail@mail.com",
                "first",
                "last",
                "+31612345679",
                new ArrayList<>());
    }

    @Test
    @DisplayName("Bill with user with username tests true")
    void billWithUser() {
        Person customer = new PersonBuilder("customer")
                .setUser(user)
                .build();
        Bill bill = new BillFactory(null, customer)
                .create();
        BillFromUserWithUsernameFilter predicate = new BillFromUserWithUsernameFilter(user.getUsername());

        boolean result = predicate.test(bill);

        assertTrue(result);
    }

    @Test
    @DisplayName("Bill with user with different username tests false")
    void billWithOtherUser() {
        Person customer = new PersonBuilder("customer")
                .setUser(user)
                .build();
        Bill bill = new BillFactory(null, customer)
                .create();
        BillFromUserWithUsernameFilter predicate = new BillFromUserWithUsernameFilter("SomeNotExistingUserName");

        boolean result = predicate.test(bill);

        assertFalse(result);
    }

    @Test
    @DisplayName("Bill without user tests false")
    void billWithoutUser() {
        Person customer = new PersonBuilder("customer")
                .build();
        Bill bill = new BillFactory(null, customer)
                .create();
        BillFromUserWithUsernameFilter predicate = new BillFromUserWithUsernameFilter("SomeNotExistingUserName");

        boolean result = predicate.test(bill);

        assertFalse(result);
    }
}