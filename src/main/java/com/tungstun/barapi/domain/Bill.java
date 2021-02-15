package com.tungstun.barapi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "price")
    private double price;

    @JsonManagedReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private Session session;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderLine> orderLines;

    public Bill() { }
    public Bill(Session session, Customer customer, List<OrderLine> orderLines, boolean isPayed, double price) {
        this.session = session;
        this.customer = customer;
        this.orderLines = orderLines;
        this.isPayed = isPayed;
        this.price = price;
        this.creationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Session getSession() { return session; }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderLine> getOrderLines() {
        return this.orderLines;
    }

    public boolean addOrderLine(OrderLine orderLine ){
        if ( !this.orderLines.contains(orderLine) ) return this.orderLines.add(orderLine);
        return false;
    }

    public boolean removeOrderLine(Order order){
        return this.orderLines.remove(order);
    }

    public boolean isPayed() { return isPayed; }

    public void setPayed(boolean payed) { isPayed = payed; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public LocalDateTime getClosedDate() { return closedDate; }

    public void setClosedDate(LocalDateTime closedDate) { this.closedDate = closedDate; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }
}
