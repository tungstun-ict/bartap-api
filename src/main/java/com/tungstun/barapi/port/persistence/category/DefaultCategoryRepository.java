package com.tungstun.barapi.port.persistence.category;

import com.tungstun.barapi.domain.product.Category;
import com.tungstun.barapi.domain.product.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultCategoryRepository implements CategoryRepository {
    private final SpringCategoryRepository repository;

    public DefaultCategoryRepository(SpringCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category save(Category entity) {
        return repository.save(entity);
    }

    @Override
    public Category update(Category entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return repository.findById(id);
    }
}
