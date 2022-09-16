package com.tungstun.barapi.port.persistence.bar;

import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultBarRepository implements BarRepository {
    private final SpringBarRepository repository;

    public DefaultBarRepository(SpringBarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Bar save(Bar bar) {
        return repository.save(bar);
    }

    @Override
    public Bar update(Bar bar) {
        return repository.save(bar);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Bar> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Bar> findAllById(Iterable<UUID> ids) {
        return repository.findAllById(ids);
    }
}
