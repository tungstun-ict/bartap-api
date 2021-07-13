package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    @Override
    @Modifying
    @Query(value = "UPDATE Product p SET p.deleted = true WHERE :entity = p")
    void delete(Product entity);
}
