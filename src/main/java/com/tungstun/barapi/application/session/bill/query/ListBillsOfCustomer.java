package com.tungstun.barapi.application.session.bill.query;

public record ListBillsOfCustomer(
        Long barId,
        Long customerId) {
}
