package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}
