package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
