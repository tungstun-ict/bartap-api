package com.tungstun.barapi.port.persistence.product;

import com.tungstun.barapi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringProductRepository extends JpaRepository<Product, UUID> {
}
