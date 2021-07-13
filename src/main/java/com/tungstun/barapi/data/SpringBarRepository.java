package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.bar.Bar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SpringBarRepository extends JpaRepository<Bar, Long> {
    Optional<Bar> findById(Long id);
    Optional<Bar> findBarByDetails_Name(String name);
}
