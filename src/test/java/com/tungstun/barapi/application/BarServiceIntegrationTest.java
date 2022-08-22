package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.data.SpringPersonRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.data.model.UserRole;
import com.tungstun.security.data.repository.SpringUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class BarServiceIntegrationTest {
    @Autowired
    private SpringUserRepository userRepository;
    @Autowired
    private SpringPersonRepository personRepository;
    @Autowired
    private SpringBarRepository repository;
    @Autowired
    private BarService service;

    private static Stream<Arguments> provideAllBars() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(new BarBuilder().build())),
                Arguments.of(List.of(new BarBuilder().build(), new BarBuilder().build()))
        );
    }
    @ParameterizedTest
    @MethodSource("provideAllBars")
    @DisplayName("Get all bars returns bars")
    void getAllBars_ReturnsBars(List<Bar> expectedBars) {
        expectedBars = repository.saveAll(expectedBars);

        List<Bar> resBars = service.getAllBars();

        assertEquals(expectedBars, resBars);
    }

    @Test
    @DisplayName("Get all bars of bar owner returns bars")
    void getAllBarsOfBarOwner_ReturnsBars()  {
        repository.save(new BarBuilder().build()); //Not owned bar
        Bar bar = repository.save(new BarBuilder().build());
        User user = new User("user", "", "", "", "", new ArrayList<>());
        user.addUserBarAuthorizations(new UserBarAuthorization(bar, user, UserRole.ROLE_BAR_OWNER));
        user = userRepository.save(user);

        List<Bar> resBars = service.getAllBarOwnerBars(user.getUsername());

        assertEquals(1, resBars.size());
        assertTrue(resBars.contains(bar));
    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws EntityNotFoundException {
        Bar bar = repository.save(new BarBuilder().build());

        Bar resBar = service.getBar(bar.getId());

        assertEquals(bar, resBar);
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        assertThrows(
                EntityNotFoundException.class,
                () -> service.getBar(999L)
        );
    }

    @Test
    @DisplayName("Create bar returns bar")
    void createBar_ReturnsBar(){
        BarRequest request = new BarRequest("address", "name", "mail", "0612345678");
        User user = userRepository.save(new User("user", "", "", "", "", new ArrayList<>()));

        assertDoesNotThrow(() -> service.addBar(request, user.getUsername()));
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        User user = userRepository.save(new User("user", "", "", "", "", new ArrayList<>()));
        Person person = personRepository.save(new Person(
                "name",
                "0612345678",
                user,
                new ArrayList<>()
        ));
        BarRequest request = new BarRequest("address", "name", "mail", "0612345678");
        Bar bar = new BarBuilder().setName(request.name).build();
        bar.addUser(person);
        repository.save(bar);
        String username = person.getUser().getUsername();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addBar(request, username)
        );
    }

    @Test
    @DisplayName("Update bar returns updated bar")
    void updateBar_ReturnsUpdatedBar() throws EntityNotFoundException {
        User user = userRepository.save(new User("user", "", "", "", "", new ArrayList<>()));
        Person person = personRepository.save(new Person(
                "name",
                "0612345678",
                user,
                new ArrayList<>()
        ));
        Bar bar = new BarBuilder().setName("name").build();
        bar.addUser(person);
        repository.save(bar);
        BarRequest request = new BarRequest("newAddress", "newName", "newMail", "0698765432");

        Bar resBar = service.updateBar(bar.getId(), request);

        assertEquals(request.name, resBar.getDetails().getName());
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound(){
        BarRequest request = new BarRequest();

        assertThrows(
                EntityNotFoundException.class,
                () -> service.updateBar(999L, request)
        );
    }

    @Test
    @DisplayName("Save bar with id saves bar")
    void saveBarWithId_SavesBar(){
        Bar bar = new BarBuilder().build();

        assertDoesNotThrow(
                () -> assertEquals(bar, service.saveBar(bar))
        );
    }

    @Test
    @DisplayName("Delete bar with id")
    void deleteBar_DeletesBar(){
        Bar bar = repository.save(new BarBuilder().build());

        assertDoesNotThrow(
                () -> service.deleteBar(bar.getId())
        );

        assertTrue(repository.findById(bar.getId()).isEmpty());
    }
}
