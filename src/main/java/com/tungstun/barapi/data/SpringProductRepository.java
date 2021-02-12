package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
