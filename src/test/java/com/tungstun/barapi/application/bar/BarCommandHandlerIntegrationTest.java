package com.tungstun.barapi.application.bar;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.command.CreateBar;
import com.tungstun.barapi.application.bar.command.DeleteBar;
import com.tungstun.barapi.application.bar.command.UpdateBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
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
public class BarCommandHandlerIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpringBarRepository repository;
    @Autowired
    private BarCommandHandler barCommandHandler;

    @Test
    @DisplayName("Create bar returns bar")
    void createBar_ReturnsBar() {
        User user = userRepository.save(new User(UUID.randomUUID(), "user", "", "", "", "", "+310612345678", new ArrayList<>()));
        CreateBar command = new CreateBar("address", "name", "mail@mail.com", "+31612345678", user.getUsername());

        assertDoesNotThrow(() -> barCommandHandler.handle(command));
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345679", new ArrayList<>());
        user = userRepository.save(user);
        Person owner = new PersonBuilder("owner").setUser(user).build();
        Bar bar = new BarBuilder("bar").setPeople(new ArrayList<>(List.of(owner))).build();
        user.newBarAuthorization(bar.getId(), owner);
        user = userRepository.save(user);
        bar = repository.save(bar);
        CreateBar command = new CreateBar("address", bar.getDetails().getName(), "mail@mail.com", "+31612345678", user.getUsername());

        assertThrows(
                DuplicateRequestException.class,
                () -> barCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Update bar returns updated bar")
    void updateBar_ReturnsUpdatedBar() throws EntityNotFoundException {
        Bar bar = new BarBuilder("bar").build();
        User user = userRepository.save(new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345679", new ArrayList<>()));
        bar.createPerson("name", user);
        repository.save(bar);
        UpdateBar command = new UpdateBar(bar.getId(), "newAddress", "newName", "newMail@mail.com", "+31612345678");

        UUID id = barCommandHandler.handle(command);

        Bar actualBar = repository.findById(id).orElseThrow();
        assertEquals(command.address(), actualBar.getDetails().getAddress());
        assertEquals(command.name(), actualBar.getDetails().getName());
        assertEquals(command.mail(), actualBar.getDetails().getMail());
        assertEquals(command.phoneNumber(), actualBar.getDetails().getPhoneNumber());
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound() {
        UpdateBar command = new UpdateBar(UUID.randomUUID(), "address", "newName", "newMail@mail.com", "+31612345678");

        assertThrows(
                EntityNotFoundException.class,
                () -> barCommandHandler.handle(command)
        );
    }

    @Test
    @DisplayName("Delete bar with id")
    void deleteBar_DeletesBar() {
        Bar bar = repository.save(new BarBuilder("bar").build());
        DeleteBar command = new DeleteBar(bar.getId());

        assertDoesNotThrow(
                () -> barCommandHandler.handle(command)
        );

        assertTrue(repository.findById(bar.getId()).isEmpty());
    }
}
