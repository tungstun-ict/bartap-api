package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringOrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);
}
