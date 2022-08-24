package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.application.bar.BarQueryHandler;
import com.tungstun.barapi.application.bar.BarService;
import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.application.UserService;
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

class BarServiceTest {
    private static final BarRepository repository = mock(BarRepository.class);
    private static final UserRepository userRepository = mock(UserRepository.class);
    private static final UserService userService = mock(UserService.class);
    private static final BarQueryHandler barQueryHandler = new BarQueryHandler(repository, userService);
    private static final BarService service = new BarService(barQueryHandler, repository, userRepository, userService);

    @AfterEach
    void teardown() {
        clearInvocations(repository, userService);
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
        BarRequest request = new BarRequest("address", "name", "mail", "+31698765432");
        String ownerName = "hans";
//        when(repository.findBarByDetails_Name(request.name))
//                .thenReturn(Optional.empty());
        Bar bar = new BarBuilder(request.name)
                .setAddress(request.address)
                .setMail(request.mail)
                .setPhoneNumber(request.phoneNumber)
                .build();
        when(repository.save(any(Bar.class)))
                .thenReturn(bar);
        when(userService.loadUserByUsername(ownerName))
                .thenReturn(new User(ownerName, "", "", "", "", "+310612345678", new ArrayList<>()));

        UUID id = service.addBar(request, ownerName);

//        assertEquals(bar, resBars);
//        verify(repository, times(1)).findBarByDetails_Name(request.name);

        verify(repository, times(2)).save(any(Bar.class));
        verify(userService, times(1)).loadUserByUsername(ownerName);
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        BarRequest request = new BarRequest("address", "name", "mail", "+31698765432");
        Person person = new Person(
                UUID.randomUUID(),
                "name",
                new User("name", "", "", "", "", "+310612345678", new ArrayList<>())
//                new ArrayList<>()
        );
        Bar bar = new BarBuilder(request.name)
                .setAddress(request.address)
                .setMail(request.mail)
                .setPhoneNumber(request.phoneNumber)
                .setPeople(new ArrayList<>(List.of(person)))
                .build();
//        when(repository.findBarByDetails_Name(request.name))
//                .thenReturn(Optional.of(bar));
        when(userService.loadUserByUsername(person.getUser().getUsername()))
                .thenReturn(person.getUser());
        String username = person.getUser().getUsername();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addBar(request, username)
        );
//        verify(repository, times(1)).findBarByDetails_Name(request.name);
        verify(userService, times(1)).loadUserByUsername(person.getUser().getUsername());
    }

    @Test
    @DisplayName("Update bar returns updated bar")
    void updateBar_ReturnsUpdatedBar() throws EntityNotFoundException {
        Person person = new Person(
                UUID.randomUUID(),
                "name",
                new User("name", "", "", "", "", "+310612345678", new ArrayList<>())
//                new ArrayList<>()
        );
        Bar bar = new BarBuilder("name")
                .setAddress("address")
                .setMail("mail")
                .setPhoneNumber("+31698765432")
                .setPeople(new ArrayList<>(List.of(person)))
                .build();
        when(repository.findById(any()))
                .thenReturn(Optional.of(bar));
        Bar expectedBar = new BarBuilder("newName")
                .setAddress("newAddress")
                .setMail("newMail")
                .setPhoneNumber("+31698765432")
                .build();
        when(repository.save(any(Bar.class)))
                .thenReturn(expectedBar);

        BarRequest request = new BarRequest("newAddress", "newName", "newMail", "+31698765432");
        UUID id = service.updateBar(any(), request);

//        assertEquals(expectedBar.getDetails().getName(), bar.getDetails().getName());
//        assertEquals(expectedBar.getDetails().getMail(), bar.getDetails().getMail());
//        assertEquals(expectedBar.getDetails().getAddress(), bar.getDetails().getAddress());
//        assertEquals(expectedBar.getDetails().getPhoneNumber(), bar.getDetails().getPhoneNumber());
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any(Bar.class));
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound() {
        when(repository.findById(any()))
                .thenReturn(Optional.empty());
        BarRequest request = new BarRequest();

        assertThrows(
                EntityNotFoundException.class,
                () -> service.updateBar(any(), request)
        );

        verify(repository, times(1)).findById(any());
    }

//    @Test
//    @DisplayName("Save bar with categoryId saves bar")
//    void saveBarWithId_SavesBar(){
//        Bar bar = new BarBuilder()
//                .setAddress("address")
//                .setName("name")
//                .setMail("mail")
//                .setPhoneNumber("phoneNumber")
//                .build();
//        when(repository.save(any()))
//                .thenReturn(bar);
//
//        assertDoesNotThrow(
//                () -> assertEquals(bar, service.saveBar(bar))
//        );
//
//        verify(repository, times(1)).save(any());
//    }

    @Test
    @DisplayName("Delete bar with categoryId")
    void deleteBar_DeletesBar() {
        assertDoesNotThrow(
                () -> service.deleteBar(any())
        );

        verify(repository, times(1)).delete(any());
    }
}