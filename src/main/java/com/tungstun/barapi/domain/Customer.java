package com.tungstun.barapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends Person {
    @Column(name = "phone_number")
    private String phoneNumber;

    @Transient
    private List<Bill> bills;

    public Customer() { super(); }
    public Customer(Long id, String name, List<Bill> bills) {
        super(id, name);
        this.bills = bills;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public boolean addBill(Bill bill ){
        if ( !this.bills.contains(bill) ) return this.bills.add(bill);
        return false;
    }

    public boolean removeBill(Bill bill){
        return this.bills.remove(bill);
    }
}
