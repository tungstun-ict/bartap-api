package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}
