package com.tungstun.barapi.domain.bill;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Order update(Order order);

    void delete(UUID id);

    Optional<Order> findById(UUID id);

    Optional<Order> findByIdAndBarId(UUID id, UUID barId);
}
