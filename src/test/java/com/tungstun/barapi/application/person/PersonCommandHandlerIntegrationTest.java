package com.tungstun.barapi.application.person;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.person.command.CreatePerson;
import com.tungstun.barapi.application.person.command.DeletePerson;
import com.tungstun.barapi.application.person.command.UpdatePerson;
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
    private PersonCommandHandler personCommandHandler;

    private Bar bar;
    private Person person;

    @BeforeEach
    void setup() {
        bar = new BarBuilder("bar").build();
        person = bar.createPerson("person");
        bar = barRepository.save(bar);
    }

    @Test
    @DisplayName("create person")
    void createPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), "newName");

        assertDoesNotThrow(() -> personCommandHandler.handle(command));
    }

    @Test
    @DisplayName("create existing person")
    void createExistingPerson() {
        CreatePerson command = new CreatePerson(bar.getId(), person.getName());

        assertThrows(
                DuplicateRequestException.class,
                () -> personCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("update person")
    void updatePerson() throws EntityNotFoundException {
        UpdatePerson command = new UpdatePerson(bar.getId(), person.getId(), "personUpdated");

        UUID id = personCommandHandler.handle(command);

        Person actualPerson = repository.findById(id).orElseThrow();
        assertEquals(command.name(), actualPerson.getName());
    }

    @Test
    @DisplayName("delete person")
    void deletePerson() throws EntityNotFoundException {
        DeletePerson command = new DeletePerson(person.getId());

        personCommandHandler.handle(command);

        assertTrue(repository.findById(person.getId()).isEmpty());
    }
}
