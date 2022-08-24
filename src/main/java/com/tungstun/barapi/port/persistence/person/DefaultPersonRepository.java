package com.tungstun.barapi.port.persistence.person;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DefaultPersonRepository implements PersonRepository {
    private final SpringPersonRepository repository;

    public DefaultPersonRepository(SpringPersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Person save(Person person) {
        return repository.save(person);
    }

    @Override
    public Person update(Person person) {
        return repository.save(person);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return repository.findById(id);
    }

//    @Override
//    public List<Person> findAllByBarId(UUID barId) {
//        return repository.findAllByBarId(barId);
//    }
}
