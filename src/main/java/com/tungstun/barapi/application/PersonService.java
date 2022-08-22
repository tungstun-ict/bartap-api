package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PersonService {
    private final SpringPersonRepository personRepository;
    private final BarService barService;

    public PersonService(SpringPersonRepository personRepository, BarService barService) {
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
        return bar.getUsers();
    }

    public Person createNewPerson(Long barId, PersonRequest personRequest) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        if (checkIfPersonExists(bar.getUsers(), personRequest.name))
            throw new DuplicateRequestException(String.format("User with name '%s' already exists", personRequest.name));
        Person person = new PersonBuilder()
                .setName(personRequest.name)
                .setPhoneNumber(personRequest.phoneNumber)
                .build();
        return savePersonToBar(bar, person);
    }

    private boolean checkIfPersonExists(List<Person> people, String name) {
        return people.stream().anyMatch(person -> person.getName().equals(name));
    }

    private Person savePersonToBar(Bar bar, Person person) {
        person = this.personRepository.save(person);
        bar.addUser(person);
        this.barService.saveBar(bar);
        return person;
    }

    public Person updatePerson(Long barId, Long personId, PersonRequest personRequest) throws EntityNotFoundException {
        Person person = getPersonOfBar(barId, personId);
        person.setName(personRequest.name);
        person.setPhoneNumber(personRequest.phoneNumber);
        return this.personRepository.save(person);
    }

    public void deletePersonFromBar(Long barId, Long personId) throws EntityNotFoundException {
        Bar bar = this.barService.getBar(barId);
        Person person = getPersonOfBar(barId, personId);
        bar.removeUser(person);
        this.barService.saveBar(bar);
    }
}
