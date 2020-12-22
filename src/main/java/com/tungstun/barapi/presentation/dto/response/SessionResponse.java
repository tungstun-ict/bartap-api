package com.tungstun.barapi.presentation.dto.response;

import com.tungstun.barapi.domain.Bartender;
import com.tungstun.barapi.domain.Bill;
import com.tungstun.barapi.domain.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class SessionResponse {
    private Long id;
    private LocalDateTime date;
    private List<Bill> bills;
    private List<Customer> customers;
    private List<Bartender> bartenders;

    public SessionResponse() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) { this.date = date; }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Bartender> getBartenders() {
        return bartenders;
    }

    public void setBartenders(List<Bartender> bartenders) {
        this.bartenders = bartenders;
    }
}
