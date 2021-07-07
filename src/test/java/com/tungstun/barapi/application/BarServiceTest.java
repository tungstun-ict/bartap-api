package com.tungstun.barapi.application;

import com.sun.jdi.request.DuplicateRequestException;
import com.tungstun.barapi.data.SpringBarRepository;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.presentation.dto.request.BarRequest;
import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserBarAuthorization;
import com.tungstun.security.data.model.UserRole;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarServiceTest {
    private static final SpringBarRepository repository = mock(SpringBarRepository.class);
    private static final UserService userService = mock(UserService.class);
    private static final BarService service= new BarService(repository, userService);

    @AfterEach
    void teardown() {
        clearInvocations(repository, userService);
    }

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
        when(repository.findAll())
                .thenReturn(expectedBars);

        List<Bar> resBars = service.getAllBars();

        assertEquals(expectedBars, resBars);
        verify(repository, times(1)).findAll();
    }

//    @ParameterizedTest
//    @MethodSource("provideAllOwnerBars")
//    @DisplayName("Get all bars of bar owner returns bars")
//    void getAllBarsOfBarOwner_ReturnsBars(String username, List<UserBarAuthorization> barAuthorizations, List<Bar> expectedBars) throws NotFoundException {
//        when(userService.loadUserByMailOrUsername(username))
//                .thenReturn(new User(username, "", "", "", "", barAuthorizations));
//        when(repository.findAll())
//                .thenReturn(expectedBars);
//
//        List<Bar> resBars = service.getAllBarOwnerBars(username);
//
//        assertEquals(expectedBars, resBars);
//    }
//    private static Stream<Arguments> provideAllOwnerBars() {
//        return Stream.of(
//                Arguments.of(
//                        "hans",
//                        List.of(),
//                        List.of(List.of(new BarBuilder().build(), new BarBuilder().build()))
//
//                        ),
//                Arguments.of(List.of(new BarBuilder().build())),
//                Arguments.of(List.of(new BarBuilder().build(), new BarBuilder().build()))
//        );
//    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws NotFoundException {
        Bar expectedBar = new BarBuilder().build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedBar));

        Bar bar = service.getBar(123L);

        assertEquals(expectedBar, bar);
        verify(repository, times(1)).findById(anyLong());
    }

    static Stream<Arguments> provideBarOwners() {
        Bar bar = new BarBuilder().build();
        Bar bar2 = new BarBuilder().build();
        return Stream.of(
                Arguments.of(
                        new User("u", "u", "u", "u", "u",
                        List.of())
                ),
                Arguments.of(
                        new User("u", "u", "u", "u", "u",
                        List.of(new UserBarAuthorization(bar, null, UserRole.ROLE_BAR_OWNER)))
                ),
                Arguments.of(
                        new User("u", "u", "u", "u", "u",
                        List.of(new UserBarAuthorization(bar, null, UserRole.ROLE_BAR_OWNER), new UserBarAuthorization(bar2, null, UserRole.ROLE_BAR_OWNER)))
                )
        );
    }

    @Test
    @DisplayName("Create bar returns bar")
    void createBar_ReturnsBar(){
        BarRequest request = new BarRequest("address", "name", "mail", "0612345678");
        String ownerName = "hans";
        when(repository.findBarByDetails_Name(request.name))
                .thenReturn(Optional.empty());
        Bar bar = new BarBuilder()
                .setAddress(request.address)
                .setName(request.name)
                .setMail(request.mail)
                .setPhoneNumber(request.phoneNumber)
                .build();
        when(repository.save(any(Bar.class)))
                .thenReturn(bar);
        when(userService.loadUserByUsername(ownerName))
                .thenReturn(new User(ownerName, "", "", "", "", new ArrayList<>()));

        Bar resBars = service.addBar(request, ownerName);

        assertEquals(bar, resBars);
        verify(repository, times(1)).findBarByDetails_Name(request.name);
        verify(repository, times(2)).save(any(Bar.class));
        verify(userService, times(1)).loadUserByUsername(ownerName);
    }

    @Test
    @DisplayName("Create existing bar throws DuplicateRequestException")
    void createBarWithExistingName_ThrowDuplicateRequest() {
        BarRequest request = new BarRequest("address", "name", "mail", "0612345678");
        Person person = new Person(
                "name",
                "0612345678",
                new User("name", "", "", "", "",  new ArrayList<>()),
                new ArrayList<>());
        Bar bar = new BarBuilder()
                .setAddress(request.address)
                .setName(request.name)
                .setMail(request.mail)
                .setPhoneNumber(request.phoneNumber)
                .build();
        bar.addUser(person);
        when(repository.findBarByDetails_Name(request.name))
                .thenReturn(Optional.of(bar));
        when(userService.loadUserByUsername(person.getUser().getUsername()))
                .thenReturn(person.getUser());
        String username = person.getUser().getUsername();

        assertThrows(
                DuplicateRequestException.class,
                () -> service.addBar(request, username)
        );
        verify(repository, times(1)).findBarByDetails_Name(request.name);
        verify(userService, times(1)).loadUserByUsername(person.getUser().getUsername());
    }

    @Test
    @DisplayName("Update bar returns updated bar")
    void updateBar_ReturnsUpdatedBar() throws NotFoundException {
        Person person = new Person(
                "name",
                "0612345678",
                new User("name", "", "", "", "",  new ArrayList<>()),
                new ArrayList<>());
        Bar bar = new BarBuilder()
                .setAddress("address")
                .setName("name")
                .setMail("mail")
                .setPhoneNumber("phoneNumber")
                .build();
        bar.addUser(person);
        when(repository.findById(any()))
                .thenReturn(Optional.of(bar));
        Bar expectedBar = new BarBuilder()
                .setAddress("newAddress")
                .setName("newName")
                .setMail("newMail")
                .setPhoneNumber("0698765432")
                .build();
        when(repository.save(any(Bar.class)))
                .thenReturn(expectedBar);

        BarRequest request = new BarRequest("newAddress", "newName", "newMail", "0698765432");
        bar = service.updateBar(any(), request);

        assertEquals(expectedBar.getDetails().getName(), bar.getDetails().getName());
        assertEquals(expectedBar.getDetails().getMail(), bar.getDetails().getMail());
        assertEquals(expectedBar.getDetails().getAddress(), bar.getDetails().getAddress());
        assertEquals(expectedBar.getDetails().getPhoneNumber(), bar.getDetails().getPhoneNumber());
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any(Bar.class));
    }

    @Test
    @DisplayName("Update not existing bar throws")
    void updateNotExistingBar_ThrowsNotFound(){
        when(repository.findById(any()))
                .thenReturn(Optional.empty());
        BarRequest request = new BarRequest();

        assertThrows(
                NotFoundException.class,
                () -> service.updateBar(any(), request)
        );

        verify(repository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Save bar with id saves bar")
    void saveBarWithId_SavesBar(){
        Bar bar = new BarBuilder()
                .setAddress("address")
                .setName("name")
                .setMail("mail")
                .setPhoneNumber("phoneNumber")
                .build();
        when(repository.save(any()))
                .thenReturn(bar);

        assertDoesNotThrow(
                () -> assertEquals(bar, service.saveBar(bar))
        );

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Delete bar with id")
    void deleteBar_DeletesBar(){
        assertDoesNotThrow(
                () -> service.deleteBar(any())
        );

        verify(repository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.getBar(123L)
        );

        verify(repository, times(1)).findById(anyLong());
    }

    @ParameterizedTest
    @MethodSource("provideBarOwners")
    @DisplayName("Get all bars owned by owner")
    void getBarsOfOwner(User user){
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
        List<Bar> actualBars =  user.getUserBarAuthorizations().stream()
                .map(UserBarAuthorization::getBar)
                .collect(Collectors.toList());

        List<Bar> bars =  service.getAllBarOwnerBars(user.getUsername());

        assertEquals(actualBars, bars);
    }
}