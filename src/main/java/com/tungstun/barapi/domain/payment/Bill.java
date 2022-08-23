package com.tungstun.barapi.domain.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    private UUID id;

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

    public Bill() {
    }

    public Bill(UUID id, Session session, boolean isPayed, Person customer, List<Order> orders) {
        this.id = id;
        this.session = session;
        this.isPayed = isPayed;
        this.customer = customer;
        this.orders = orders;
    }

    public double calculateTotalPrice() {
        return this.orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice().amount().doubleValue() * order.getAmount())
                .sum();
    }

    public Order addOrder(Product product, int amount, Person bartender) {
        session.checkEditable();
        System.out.println(product);
        System.out.println(amount);
        System.out.println(bartender);
        System.out.println(product == null || amount < 1 || bartender == null);
        if (product == null || amount < 1 || bartender == null) {
            throw new IllegalArgumentException("Amount must be a positive number and product and bartender must not be empty");
        }

        Order order = new OrderFactory(product, amount, bartender).create();
        orders.add(order);
        return order;
    }

    public boolean removeOrder(UUID orderId) {
        session.checkEditable();
        return orders.removeIf(order -> order.getId().equals(orderId));
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public UUID getId() {
        return id;
    }

    public Person getCustomer() {
        return customer;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void pay() {
        isPayed = true;
    }
}
