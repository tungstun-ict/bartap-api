package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.person.PersonCommandHandler;
import com.tungstun.barapi.application.person.PersonQueryHandler;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.DeletePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.port.persistence.person.SpringPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PersonCommandHandlerIntegrationTest {
    @Autowired
    private SpringPersonRepository repository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private PersonCommandHandler service;
    @Autowired
    private PersonQueryHandler personQueryHandler;

    private Bar bar;
    private Person person;
    private Person person2;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("bar").build();
        person = bar.createPerson("person");
        person2 = bar.createPerson("person2");
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get person of bar")
    void getPersonOfBar() throws EntityNotFoundException {
        Person resPerson = personQueryHandler.handle(new GetPerson(person.getId(), bar.getId()));

        assertEquals(person, resPerson);
    }

    @Test
    @DisplayName("Get not existing person")
    void getNotExistingPersonOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> personQueryHandler.handle(new GetPerson(UUID.randomUUID(), bar.getId()))
        );
    }

    @Test
    @DisplayName("Get all people of bar")
    void getPeopleOfBar() throws EntityNotFoundException {
        List<Person> resPeople = personQueryHandler.handle(new ListPeopleOfBar(bar.getId()));

        assertEquals(2, resPeople.size());
        assertTrue(resPeople.contains(person));
        assertTrue(resPeople.contains(person2));
    }

    @Test
    @DisplayName("Get all people of bar when none")
    void getPeopleOfBarWhenNone() throws EntityNotFoundException {
        bar.removePerson(person);
        bar.removePerson(person2);
        barRepository.save(bar);

        List<Person> resPeople = personQueryHandler.handle(new ListPeopleOfBar(bar.getId()));

        assertTrue(resPeople.isEmpty());
    }

    @Test
    @DisplayName("create person")
    void createPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), "newName");

        assertDoesNotThrow(() -> service.createNewPerson(command));
    }

    @Test
    @DisplayName("create existing person")
    void createExistingPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), person.getName());

        assertThrows(
                DuplicateRequestException.class,
                () -> service.createNewPerson(command)
        );
    }

    @Test
    @DisplayName("update person")
    void updatePerson() throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(bar.getId(), person.getId(), "personUpdated");

        UUID id = service.updatePerson(command);

        Person actualPerson = repository.findById(id).orElseThrow();
        assertEquals(command.name(), actualPerson.getName());
    }

    @Test
    @DisplayName("delete person")
    void deletePerson() throws EntityNotFoundException {
        DeletePerson command = new DeletePerson(person.getId());

        service.deletePersonFromBar(command);

        assertTrue(repository.findById(person.getId()).isEmpty());
    }
}