package com.tungstun.barapi.application.person;

import com.tungstun.barapi.application.person.query.GetPerson;
import com.tungstun.barapi.application.person.query.ListPeopleOfBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
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
class PersonQueryHandlerIntegrationTest {
    @Autowired
    private SpringBarRepository barRepository;
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
        Person resPerson = personQueryHandler.handle(new GetPerson(bar.getId(), person.getId()));

        assertEquals(person, resPerson);
    }

    @Test
    @DisplayName("Get not existing person")
    void getNotExistingPersonOfBar() {
        assertThrows(
                EntityNotFoundException.class,
                () -> personQueryHandler.handle(new GetPerson(bar.getId(), UUID.randomUUID()))
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
}
