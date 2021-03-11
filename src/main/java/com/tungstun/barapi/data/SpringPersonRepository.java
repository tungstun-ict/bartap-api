package com.tungstun.barapi.data;

import com.tungstun.barapi.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringPersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findById(Long id);
}
