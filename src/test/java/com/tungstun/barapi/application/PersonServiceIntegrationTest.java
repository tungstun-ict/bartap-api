package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.presentation.dto.request.PersonRequest;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PersonServiceIntegrationTest {
    @Autowired
    private SpringPersonRepository repository;
    @Autowired
    private SpringBarRepository barRepository;
    @Autowired
    private PersonService service;

    private Bar bar;
    private Person person;
    private Person person2;

    @BeforeEach
    void setup() {
        bar = new BarBuilder().build();
        person = new PersonBuilder().setName("person").setPhoneNumber("0612345678").build();
        person2 = new PersonBuilder().setName("person2").setPhoneNumber("0687654321").build();
        person = repository.save(person);
        person2 = repository.save(person2);
        bar.addUser(person);
        bar.addUser(person2);
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("Get person of bar")
    void getPersonOfBar() throws NotFoundException {
        Person resPerson = service.getPersonOfBar(bar.getId(), person.getId());

        assertEquals(person, resPerson);
    }

    @Test
    @DisplayName("Get not existing person")
    void getNotExistingPersonOfBar() {
        assertThrows(
                NotFoundException.class,
                () -> service.getPersonOfBar(bar.getId(), 999L)
        );
    }

    @Test
    @DisplayName("Get all people of bar")
    void getPeopleOfBar() throws NotFoundException {
        List<Person> resPeople = service.getAllPeopleOfBar(bar.getId());

        assertEquals(2, resPeople.size());
        assertTrue(resPeople.contains(person));
        assertTrue(resPeople.contains(person2));
    }

    @Test
    @DisplayName("Get all people of bar when none")
    void getPeopleOfBarWhenNone() throws NotFoundException {
        bar.removeUser(person);
        bar.removeUser(person2);
        barRepository.save(bar);

        List<Person> resPeople = service.getAllPeopleOfBar(bar.getId());

        assertTrue(resPeople.isEmpty());
    }

    @Test
    @DisplayName("create person")
    void createPerson() {
        PersonRequest request = new PersonRequest();
        request.name = "newPerson";
        request.phoneNumber = "0601010101";

        assertDoesNotThrow(() -> service.createNewPerson(bar.getId(), request));
    }

    @Test
    @DisplayName("create existing person")
    void createExistingPerson() {
        PersonRequest request = new PersonRequest();
        request.name = "person";
        request.phoneNumber = "0612345678";

        assertThrows(
                DuplicateRequestException.class,
                () -> service.createNewPerson(bar.getId(), request)
        );
    }

    @Test
    @DisplayName("update person")
    void updatePerson() throws NotFoundException {
        PersonRequest request = new PersonRequest();
        request.name = "personUpdated";
        request.phoneNumber = "0601010101";

        Person resPerson = service.updatePerson(bar.getId(), person.getId(), request);

        assertEquals(request.name, resPerson.getName());
        assertEquals(request.phoneNumber, resPerson.getPhoneNumber());
    }

    @Test
    @DisplayName("delete person")
    void deletePerson() throws NotFoundException {
        service.deletePersonFromBar(bar.getId(), person.getId());

        Bar resBar = barRepository.findById(bar.getId()).get();
        assertFalse(resBar.getUsers().contains(person));
    }
}