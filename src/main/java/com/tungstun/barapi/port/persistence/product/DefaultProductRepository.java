package com.tungstun.barapi.port.persistence.product;

import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultProductRepository implements ProductRepository {
    private final SpringProductRepository repository;

    public DefaultProductRepository(SpringProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product entity) {
        return repository.save(entity);
    }

    @Override
    public Product update(Product entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id);
    }
}
