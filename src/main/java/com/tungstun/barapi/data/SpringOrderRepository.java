package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.payment.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringOrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);
}
