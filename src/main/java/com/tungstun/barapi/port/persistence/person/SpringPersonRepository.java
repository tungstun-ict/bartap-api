package com.tungstun.barapi.port.persistence.person;

import com.tungstun.barapi.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringPersonRepository extends JpaRepository<Person, UUID> {
}
