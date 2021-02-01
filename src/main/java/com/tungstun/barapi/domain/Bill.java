package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_payed")
    private boolean isPayed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private Session session;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Transient
    private List<Order> orders;

    public Bill() { }
    public Bill(Session session, Customer customer, List<Order> orders, boolean isPayed) {
        this.session = session;
        this.customer = customer;
        this.orders = orders;
        this.isPayed = isPayed;
    }

    public Long getId() {
        return id;
    }

    public Session getSession() { return session; }

    public Customer getCustomer() {
        return customer;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public boolean addBartender(Order order ){
        if ( !this.orders.contains(order) ) return this.orders.add(order);
        return false;
    }

    public boolean removeBartender(Order order){
        return this.orders.remove(order);
    }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }
}
