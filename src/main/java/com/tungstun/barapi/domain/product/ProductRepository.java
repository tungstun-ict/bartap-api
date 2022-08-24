package com.tungstun.barapi.domain.product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Product update(Product product);

    void delete(UUID id);

    Optional<Product> findById(UUID id);

//    Optional<Product> findByIdAndBarId(UUID id, UUID barId);
//
//    List<Product> findAllOfBar(UUID barId);
//
//    List<Product> findAllOfCategory(UUID categoryId, UUID barId);
//
//    void delete(UUID id, UUID barId);
}
