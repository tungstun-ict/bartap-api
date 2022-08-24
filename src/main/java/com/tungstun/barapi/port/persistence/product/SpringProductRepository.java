package com.tungstun.barapi.port.persistence.product;

import com.tungstun.barapi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringProductRepository extends JpaRepository<Product, UUID> {
//    Optional<Product> findByIdAndBarId(UUID id, UUID barId);
//
//    List<Product> findAllByBarId(UUID barId);
//
//    List<Product> findAllByCategoryIdAndBarId(UUID categoryId, UUID barId);
//
//    void deleteByIdAndBarId(UUID id, UUID barId);
}
