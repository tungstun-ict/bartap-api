package com.tungstun.barapi.port.persistence.category;

import com.tungstun.barapi.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringCategoryRepository extends JpaRepository<Category, UUID> {
}
