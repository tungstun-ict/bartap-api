package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_history_entry")
public class OrderHistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_history_type")
    @Enumerated(value = EnumType.STRING)
    private OrderHistoryType type;

    @Column(name = "order_date")
    private LocalDateTime date;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    private Person customer;

    @ManyToOne
    private Person bartender;

    public OrderHistoryEntry() {
    }

    public OrderHistoryEntry(OrderHistoryType type, LocalDateTime date, UUID productId, String productName, Integer amount, Person customer, Person bartender) {
        this.type = type;
        this.date = date;
        this.productId = productId;
        this.productName = productName;
        this.amount = amount;
        this.customer = customer;
        this.bartender = bartender;
    }

    public Long getId() {
        return id;
    }

    public OrderHistoryType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getAmount() {
        return amount;
    }

    public Person getCustomer() {
        return customer;
    }

    public Person getBartender() {
        return bartender;
    }
}
