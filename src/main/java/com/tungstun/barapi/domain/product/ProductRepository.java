package com.tungstun.barapi.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Product update(Product product);

    void delete(Long id);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdAndBarId(Long id, Long barId);

    List<Product> findAllOfBar(Long barId);

    List<Product> findAllOfCategory(Long categoryId, Long barId);

    void delete(Long id, Long barId);
}
