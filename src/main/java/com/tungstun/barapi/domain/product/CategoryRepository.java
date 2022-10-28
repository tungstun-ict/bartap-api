
package com.tungstun.barapi.domain.product;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);

    Category update(Category category);

    void delete(UUID id);

    Optional<Category> findById(UUID id);
}
