package com.tungstun.barapi.port.persistence.category;

import com.tungstun.barapi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndBarId(Long id, Long barId);

    List<Category> findAllByBarId(Long barId);
}
