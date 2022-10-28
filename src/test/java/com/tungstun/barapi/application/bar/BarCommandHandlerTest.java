package com.tungstun.barapi.application.bar;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.command.CreateBar;
import com.tungstun.barapi.application.bar.command.DeleteBar;
import com.tungstun.barapi.application.bar.command.UpdateBar;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarDetails;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.common.phonenumber.PhoneNumber;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarCommandHandlerTest {
    private static final BarRepository repository = mock(BarRepository.class);
    private static final UserRepository userRepository = mock(UserRepository.class);
    private static final UserQueryHandler userQueryHandler = mock(UserQueryHandler.class);
    private static final BarQueryHandler barQueryHandler = new BarQueryHandler(repository, userQueryHandler);
    private static final BarCommandHandler service = new BarCommandHandler(barQueryHandler, repository, userRepository, userQueryHandler);

    @AfterEach
    void teardown() {
        clearInvocations(repository, userQueryHandler, userRepository);
    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws EntityNotFoundException {
        Bar expectedBar = new BarBuilder("bar").build();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(expectedBar));

        Bar bar = barQueryHandler.handle(new GetBar(UUID.randomUUID()));

        assertEquals(expectedBar, bar);
        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> barQueryHandler.handle(new GetBar(UUID.randomUUID()))
        );

        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Create bar returns bar")
    void createBar_ReturnsBar() {
        String ownerName = "hans";
        CreateBar command = new CreateBar("address 0", "name", "mail@bar.com", "+31687654321", ownerName);
        Bar bar = new BarBuilder("bar")
                .setAddress("address 1")
                .setMail("new@mail.com")
                .setPhoneNumber("+31687654321")
                .build();
        when(userQueryHandler.loadUserByUsername(ownerName))
                .thenReturn(new User(UUID.randomUUID(), ownerName, "", "", "", "", "+31687654321", new ArrayList<>()));
        when(repository.findAllById(any()))
                .thenReturn(new ArrayList<>(List.of(bar)));
        when(repository.save(any(Bar.class)))
                .thenReturn(bar);

        assertDoesNotThrow(() -> service.handle(command));

        verify(userQueryHandler, times(1)).loadUserByUsername(ownerName);
        verify(repository, times(1)).findAllById(any());
        verify(repository, times(1)).save(any(Bar.class));
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        Person person = new Person(
                UUID.randomUUID(),
                "name",
                new User(UUID.randomUUID(), "name", "", "", "", "", "+310612345678", new ArrayList<>())
        );
        CreateBar command = new CreateBar("address", "name", "mail", "+31687654321", person.getUser().getUsername());
        Bar bar = new BarBuilder(command.name())
                .setAddress(command.address())
                .setMail(command.mail())
                .setPhoneNumber(command.phoneNumber())
                .setPeople(new ArrayList<>(List.of(person)))
                .build();
        person.getUser().newBarAuthorization(bar.getId(), person);
        String username = person.getUser().getUsername();
        when(userQueryHandler.loadUserByUsername(username))
                .thenReturn(person.getUser());
        when(repository.findAllById(any()))
                .thenReturn(new ArrayList<>(List.of(bar)));

        assertThrows(
                DuplicateRequestException.class,
                () -> service.handle(command)
        );
        verify(repository, times(1)).findAllById(any());
        verify(userQueryHandler, times(1)).loadUserByUsername(person.getUser().getUsername());
    }

    @Test
    @DisplayName("Update bar returns updated bar")
    void updateBar_ReturnsUpdatedBar() throws EntityNotFoundException {
        Person person = new Person(
                UUID.randomUUID(),
                "name",
                new User(UUID.randomUUID(), "name", "", "", "", "", "+31612345678", new ArrayList<>())
        );
        Bar bar = new BarBuilder("name")
                .setAddress("address")
                .setMail("mail")
                .setPhoneNumber("+31687654321")
                .setPeople(new ArrayList<>(List.of(person)))
                .build();
        when(repository.findById(bar.getId()))
                .thenReturn(Optional.of(bar));
        Bar expectedBar = new Bar(bar.getId(), new BarDetails("newAddress", "newName", "newMail@Mmail.com", new PhoneNumber("+31687654321")), List.of(), List.of(), List.of(), List.of());
        when(repository.save(any(Bar.class)))
                .thenReturn(expectedBar);
        UpdateBar command = new UpdateBar(bar.getId(), "newAddress", "newName", "newMail@mail.com", "+31612345678");

        service.handle(command);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any(Bar.class));
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound() {
        when(repository.findById(any()))
                .thenReturn(Optional.empty());
        UpdateBar command = new UpdateBar(UUID.randomUUID(), "newAddress", "newName", "newMail@mail.com", "+31612345678");


        assertThrows(
                EntityNotFoundException.class,
                () -> service.handle(command)
        );

        verify(repository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Delete bar with id")
    void deleteBar_DeletesBar() {
        DeleteBar command = new DeleteBar(UUID.randomUUID());

        assertDoesNotThrow(
                () -> service.handle(command)
        );

        verify(repository, times(1)).delete(any());
    }
}