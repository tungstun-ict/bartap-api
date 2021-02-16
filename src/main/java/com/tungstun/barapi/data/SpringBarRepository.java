package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.bar.Bar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringBarRepository extends JpaRepository<Bar, Long> {
    Optional<Bar> findById(Long id);
    Optional<Bar> findBarByName(String name);
}
