package com.tungstun.barapi.domain;

import java.util.List;

public class Customer extends User{
    private String phoneNumber;
    private List<Bill> bills;


    public Customer(String id, String name, List<Bill> bills) {
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
        if ( !this.bills.contains(bill) ){
            return this.bills.add(bill);
        }
        return false;

    }
    public boolean removeBill(Bill bill){
        return this.bills.remove(bill);
    }
}
