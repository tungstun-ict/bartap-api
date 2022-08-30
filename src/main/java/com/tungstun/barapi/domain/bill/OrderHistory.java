package com.tungstun.barapi.domain.bill;

import com.tungstun.barapi.domain.person.Person;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderHistory {
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderHistoryEntry> history;

    public OrderHistory() {
    }

    public OrderHistory(List<OrderHistoryEntry> history) {
        this.history = history;
    }

    public boolean addEntry(Order order, Person customer) {
        OrderProduct product = order.getProduct();
        return history.add(new OrderHistoryEntry(
                order.getCreationDate(),
                product.getId(),
                String.format("%s %s", product.getBrand(), product.getName()),
                order.getAmount(),
                customer,
                order.getBartender()
        ));
    }

    public List<OrderHistoryEntry> getHistory() {
        return history;
    }
}
