package com.tungstun.barapi.domain.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tungstun.barapi.domain.person.Person;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    private UUID id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Embedded
    private OrderProduct product;

    @Column(name = "amount")
    private int amount;

    @OneToOne
    private Person bartender;


    public Order() {
    }

    public Order(UUID id, OrderProduct product, int amount, Person bartender) {
        this.id = id;
        this.creationDate = ZonedDateTime.now().toLocalDateTime();
        this.product = product;
        this.amount = amount;
        this.bartender = bartender;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public OrderProduct getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public Person getBartender() {
        return bartender;
    }
}
