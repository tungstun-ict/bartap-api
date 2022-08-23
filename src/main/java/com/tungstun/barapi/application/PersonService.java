package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final BarService barService;

    public PersonService(PersonRepository personRepository, BarService barService) {
        this.personRepository = personRepository;
        this.barService = barService;
    }

    public Person getPersonOfBar(Long barId, Long personId) throws EntityNotFoundException {
        List<Person> people = getAllPeopleOfBar(barId);
        return people.stream()
                .filter(person -> person.getId().equals(personId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Bar does not have a person with id: " + personId));
    }

    public List<Person> getAllPeopleOfBar(Long barId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        return bar.getPeople();
    }

    public Person createNewPerson(Long barId, PersonRequest personRequest) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        if (checkIfPersonExists(bar.getPeople(), personRequest.name))
            throw new DuplicateRequestException(String.format("User with name '%s' already exists", personRequest.name));
        Person person = new PersonBuilder(bar.getId(), personRequest.name)
                .build();
        return savePersonToBar(bar, person);
    }

    private boolean checkIfPersonExists(List<Person> people, String name) {
        return people.stream().anyMatch(person -> person.getName().equals(name));
    }

    private Person savePersonToBar(Bar bar, Person person) {
        person = this.personRepository.save(person);
        bar.addPerson(person);
        this.barService.saveBar(bar);
        return person;
    }

    public Person updatePerson(Long barId, Long personId, PersonRequest personRequest) throws EntityNotFoundException {
        Person person = getPersonOfBar(barId, personId);
        person.setName(personRequest.name);
        return this.personRepository.save(person);
    }

    public void deletePersonFromBar(Long barId, Long personId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        Person person = getPersonOfBar(barId, personId);
        bar.removePerson(person);
        this.barService.saveBar(bar);
    }
}
