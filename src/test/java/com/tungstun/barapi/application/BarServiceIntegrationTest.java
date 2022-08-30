package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.BarService;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListOwnedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonRepository;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class BarServiceIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SpringBarRepository repository;
    @Autowired
    private BarService service;
    @Autowired
    private BarQueryHandler barQueryHandler;

//    private static Stream<Arguments> provideAllBars() {
//        return Stream.of(
//                Arguments.of(List.of()),
//                Arguments.of(List.of(new BarBuilder("bar").build())),
//                Arguments.of(List.of(new BarBuilder("bar").build(), new BarBuilder("bar2").build()))
//        );
//    }
//    @ParameterizedTest
//    @MethodSource("provideAllBars")
//    @DisplayName("Get all bars returns bars")
//    void getAllBars_ReturnsBars(List<Bar> expectedBars) {
//        expectedBars = repository.saveAll(expectedBars);
//
//        List<Bar> resBars = barQueryHandler.handle(new ListOwnedBars(u));
//
//        assertEquals(expectedBars, resBars);
//    }

    @Test
    @DisplayName("Get all bars of bar owner returns bars")
    void getAllBarsOfBarOwner_ReturnsBars()  {
        repository.save(new BarBuilder("bar").build()); //Not owned bar
        Bar bar = repository.save(new BarBuilder("bar2").build());
        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
        user.newBarAuthorization(bar.getId());
        user = userRepository.save(user);

        List<Bar> resBars = barQueryHandler.handle(new ListOwnedBars(user.getUsername()));

        assertEquals(1, resBars.size());
        assertTrue(resBars.contains(bar));
    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws EntityNotFoundException {
        Bar bar = repository.save(new BarBuilder("bar").build());

        Bar resBar = barQueryHandler.handle(new GetBar(bar.getId()));

        assertEquals(bar, resBar);
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        assertThrows(
                EntityNotFoundException.class,
                () -> barQueryHandler.handle(new GetBar(UUID.randomUUID()))
        );
    }

    @Test
    @DisplayName("Create bar returns bar")
    void createBar_ReturnsBar(){
        BarRequest request = new BarRequest("address", "name", "mail", "+31612345678");
        User user = userRepository.save(new User("user", "", "", "", "", "+310612345678", new ArrayList<>()));

        assertDoesNotThrow(() -> service.addBar(request, user.getUsername()));
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        BarRequest request = new BarRequest("address", "name", "mail", "+31661234567");
        Bar bar = new BarBuilder("bar").setName(request.name).build();
        User user = new User("user", "", "", "", "", "+31612345679", new ArrayList<>());
        user.newBarAuthorization(bar.getId());
        user = userRepository.save(user);
        Person person = bar.createPerson("name", user);

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
        BarRequest request = new BarRequest("newAddress", "newName", "newMail", "+31612345678");
        Bar bar = new BarBuilder("bar").setName("name").build();
        User user = userRepository.save(new User("user", "", "", "", "", "+31612345679", new ArrayList<>()));
        bar.createPerson("name", user);
        repository.save(bar);

        UUID id = service.updateBar(bar.getId(), request);

//        assertEquals(request.name, resBar.getDetails().getName());
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound(){
        BarRequest request = new BarRequest();

        assertThrows(
                EntityNotFoundException.class,
                () -> service.updateBar(UUID.randomUUID(), request)
        );
    }
//
//    @Test
//    @DisplayName("Save bar with categoryId saves bar")
//    void saveBarWithId_SavesBar(){
//        Bar bar = new BarBuilder("bar").build();
//
//        assertDoesNotThrow(
//                () -> assertEquals(bar, service.saveBar(bar))
//        );
//    }

    @Test
    @DisplayName("Delete bar with id")
    void deleteBar_DeletesBar(){
        Bar bar = repository.save(new BarBuilder("bar").build());

        assertDoesNotThrow(
                () -> service.deleteBar(bar.getId())
        );

        assertTrue(repository.findById(bar.getId()).isEmpty());
    }
}
