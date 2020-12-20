package com.tungstun.barapi.domain;

import java.util.Date;
import java.util.List;

public class Session {

    private String id;
    private Date date;
    private Bill bill;
    private List<Customer> customers;
    private List<Bartender> bartenders;


    public Session(String id, Date date, Bill bill, List<Customer> customers, List<Bartender> bartenders) {
        this.id = id;
        this.date = date;
        this.bill = bill;
        this.customers = customers;
        this.bartenders = bartenders;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Bill getBill() {
        return bill;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public boolean addCustomer(Customer customer){
        if ( !this.customers.contains(customer) ){
            return this.customers.add(customer);
        }
        return false;

    }
    public boolean removeCustomer(Customer customer){
        return this.customers.remove(customer);
    }


    public List<Bartender> getBartenders() {
        return this.bartenders;
    }

    public boolean addBartender(Bartender bartender){
        if ( !this.bartenders.contains(bartender) ){
            return this.bartenders.add(bartender);
        }
        return false;

    }
    public boolean removeBartender(Bartender bartender){
        return this.bartenders.remove(bartender);
    }

}
