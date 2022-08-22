
package com.tungstun.barapi.domain.product;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Category update(Category category);

    void delete(Long id);

    Optional<Category> findById(Long id);

    Optional<Category> findByIdAndBarId(Long id, Long barId);

    List<Category> findAllOfBar(Long barId);
}
