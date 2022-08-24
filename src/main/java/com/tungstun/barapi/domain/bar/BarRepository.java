package com.tungstun.barapi.domain.bar;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BarRepository {
    Bar save(Bar bar);

    Bar update(Bar bar);

    void delete(UUID id);

    Optional<Bar> findById(UUID id);

    List<Bar> findAllById(Iterable<UUID> ids);
}
