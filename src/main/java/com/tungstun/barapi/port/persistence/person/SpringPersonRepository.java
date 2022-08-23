package com.tungstun.barapi.port.persistence.person;

import com.tungstun.barapi.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringPersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByBarId(Long barId);
}
