package com.tungstun.statistics.domain.statistics.filter;

import com.tungstun.barapi.domain.bill.Bill;
import com.tungstun.security.domain.user.User;

import java.util.function.Predicate;

public record BillFromUserWithUsernameFilter(String username) implements Predicate<Bill> {
    @Override
    public boolean test(Bill bill) {
        User user = bill.getCustomer().getUser();
        return user != null && user.getUsername().equals(username);
    }
}
