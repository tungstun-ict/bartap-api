package com.tungstun.barapi.domain.bill;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.session.Session;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "bill")
@SQLDelete(sql = "UPDATE bill SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Bill {
    @Column(name = "deleted", columnDefinition = "BOOLEAN default false")
    private final boolean deleted = Boolean.FALSE;

    @Id
    private UUID id;

    @Column(name = "is_payed")
    private boolean isPayed;

    @ManyToOne
    @JoinColumn(name = "session_id")
//    @Where(clause = "deleted = false")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistoryEntry> history;

    public Bill() {
    }

    public Bill(UUID id, Session session, boolean isPayed, Person customer, List<Order> orders) {
        this.id = id;
        this.session = session;
        this.isPayed = isPayed;
        this.customer = customer;
        this.orders = orders;
        this.history = new ArrayList<>();
    }

    public double calculateTotalPrice() {
        return this.orders.stream()
                .mapToDouble(Order::orderPrice)
                .sum();
    }

    private boolean addHistoryEntry(OrderHistoryType type, Order order, Person customer) {
        OrderProduct product = order.getProduct();
        return history.add(new OrderHistoryEntry(
                UUID.randomUUID(),
                type,
                order.getCreationDate(),
                product.getId(),
                String.format("%s %s", product.getBrand(), product.getName()),
                order.getAmount(),
                customer,
                order.getBartender()
        ));
    }

    public Order addOrder(Product product, int amount, Person bartender) {
        session.checkEditable();
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (bartender == null) throw new IllegalArgumentException("Bartender cannot be null");
        if (amount < 1) throw new IllegalArgumentException("Amount of products must be above 0");

        Order order = new OrderFactory(product, amount, bartender).create();
        orders.add(order);
        addHistoryEntry(OrderHistoryType.ADD, order, customer);
        return order;
    }

    public void removeOrder(UUID orderId) {
        session.checkEditable();
        Order order = orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("No order found with id: " + orderId));
        orders.remove(order);
        addHistoryEntry(OrderHistoryType.REMOVE, order, customer);
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

    public Session getSession() {
        return session;
    }

    public List<OrderHistoryEntry> getHistory() {
        return Collections.unmodifiableList(history);
    }
}
