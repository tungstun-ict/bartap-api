package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringOrderLineRepository extends JpaRepository<OrderLine, Long> {
    Optional<OrderLine> findById(Long id);
}
