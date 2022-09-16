package com.tungstun.barapi.domain.product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Product update(Product product);

    void delete(UUID id);

    Optional<Product> findById(UUID id);
}
