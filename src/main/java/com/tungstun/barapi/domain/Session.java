package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;


    @OneToMany(mappedBy = "session",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Bill> bills;

    @Transient
    private List<Customer> customers;

    @Transient
    private List<Bartender> bartenders;

    public Session() {}
    public Session(LocalDateTime date, List<Bill> bills, List<Customer> customers, List<Bartender> bartenders) {
        this.date = date;
        this.bills = bills;
        this.customers = customers;
        this.bartenders = bartenders;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public boolean addBill(Bill bill){
        if ( !this.bills.contains(bill) ) return this.bills.add(bill);
        return false;
    }

    public boolean removeBill(Bill bill){ return this.bills.remove(bill); }

    public List<Customer> getCustomers() { return this.customers; }

    public boolean addCustomer(Customer customer){
        if ( !this.customers.contains(customer) )return this.customers.add(customer);
        return false;
    }

    public boolean removeCustomer(Customer customer){ return this.customers.remove(customer); }

    public List<Bartender> getBartenders() { return this.bartenders; }

    public boolean addBartender(Bartender bartender){
        if ( !this.bartenders.contains(bartender) ) return this.bartenders.add(bartender);
        return false;
    }

    public boolean removeBartender(Bartender bartender){ return this.bartenders.remove(bartender); }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", date=" + date +
                ", bills=" + bills +
                ", customers=" + customers +
                ", bartenders=" + bartenders +
                '}';
    }
}
