package com.tungstun.barapi.domain.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;

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
    private Person customer;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders;

    public Bill() { }
    public Bill(Session session, Person customer, List<Order> orders, boolean isPayed) {
        this.session = session;
        this.customer = customer;
        this.orders = orders;
        this.isPayed = isPayed;
    }

    public Long getId() { return id; }

    public Session getSession() { return session; }

    public void setSession(Session session) { this.session = session; }

    public Person getCustomer() { return customer; }

    public List<Order> getOrders() { return this.orders; }

    public boolean addOrder(Product product, int amount, Person bartender){
        if (product == null || amount < 1 || bartender == null) return false;
        Order order = new Order(product, amount, bartender);
        return this.orders.add(order);
    }

    public boolean addOrder(Order order){
        if ( !this.orders.contains(order) ) return this.orders.add(order);
        return false;
    }

    public boolean removeOrder(Order order){
        return this.orders.remove(order);
    }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public double calculateTotalPrice() {
        return this.orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getAmount())
                .sum();
    }
}
