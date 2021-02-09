package com.tungstun.barapi.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "price")
    private double price;

    @OneToOne(
            cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH},
            orphanRemoval = true
    )
    private Bartender bartender;

    @Transient
    private List<OrderLine> orderLines;

    public Order() { }
    public Order(LocalDateTime date, double price, Bartender bartender, List<OrderLine> orderLines) {
        this.date = date;
        this.price = price;
        this.bartender = bartender;
        this.orderLines = orderLines;
    }

    public Long getId() { return id; }

    public LocalDateTime getDate() { return date; }

    public double getPrice() { return price; }

    public Bartender getBartender() { return bartender; }

    public List<OrderLine> getOrderLines() { return orderLines; }

    public boolean addOrderLine(OrderLine orderLine) {
        if( !this.orderLines.contains(orderLine) ) return this.orderLines.add(orderLine);
        return false;
    }

    public boolean removeOrderLine(OrderLine orderLine) {return this.orderLines.remove(orderLine); }
}
