package com.tungstun.barapi.port.persistence.bar;

import com.tungstun.barapi.domain.bar.Bar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringBarRepository extends JpaRepository<Bar, UUID> {
    Optional<Bar> findBySlug(String slug);
}
