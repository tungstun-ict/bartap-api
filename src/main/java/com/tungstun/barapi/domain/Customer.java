package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends Person {
    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "customer",
            targetEntity = Bill.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Bill> bills;

    public Customer() { super(); }
    public Customer( String name, String phoneNumber, List<Bill> bills) {
        super(name);
        this.phoneNumber = phoneNumber;
        this.bills = bills;
    }
    public Customer( Long id, String name, String phoneNumber, List<Bill> bills) {
        super(id, name);
        this.phoneNumber = phoneNumber;
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

    public void setBills(List<Bill> bills){
        this.bills = bills;
    }

    public boolean removeBill(Bill bill){
        return this.bills.remove(bill);
    }
}
