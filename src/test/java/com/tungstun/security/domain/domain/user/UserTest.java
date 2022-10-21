package com.tungstun.security.domain.domain.user;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.barapi.domain.person.PersonBuilder;
import com.tungstun.exception.NotAuthorizedException;
import com.tungstun.security.domain.user.Authorization;
import com.tungstun.security.domain.user.Role;
import com.tungstun.security.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private UUID barId;
    private User userWithAuthorization;
    private User user;

    private static Stream<Arguments> provideRolesForChangingRoles() {
        return Stream.of(
                Arguments.of(Role.CUSTOMER, Role.BARTENDER),
                Arguments.of(Role.BARTENDER, Role.CUSTOMER)
        );
    }

    @Test
    void userWithAuthorization_ContainsAuthorization() {
        String actualRole = userWithAuthorization.getAuthorizations().get(barId);

        assertEquals(Role.OWNER.name(), actualRole);
    }

    @Test
    void userWithoutAuthorization_HasNoAuthorizations() {
        Map<UUID, String> auths = user.getAuthorizations();

        assertTrue(auths.isEmpty());
    }

//  Commented due to not having functionality to check if other use already owns bar in Domain layer
//    @Test
//    void addNewBarAuthorization_WhenBarIsOwnedByOtherUser_Throws() {
//        Long barIdentification = 123L;
//        User testUser = new User("username", "password", "mail@mail.com", "first", "last", new ArrayList<>());
//        testUser.newBarAuthorization(barIdentification);
//
//        assertThrows(
//                SomeException.class,
//                () -> user.newBarAuthorization(barIdentification)
//        );
//    }

    @BeforeEach
    void setUp() {
        barId = UUID.randomUUID();
        userWithAuthorization = new User(
                UUID.randomUUID(),
                "username",
                "password",
                "mail@mail.com",
                "first",
                "last",
                "+31612345678",
                new ArrayList<>(List.of(new Authorization(UUID.randomUUID(), barId, Role.OWNER, null))));
        user = new User(
                UUID.randomUUID(),
                "username",
                "password",
                "mail@mail.com",
                "first",
                "last",
                "+31612345679",
                new ArrayList<>());
    }

    @Test
    void addNewBarAuthorization_Successfully() {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person owner = new PersonBuilder("owner").setUser(user).build();
        UUID newBarId = UUID.randomUUID();

        user.newBarAuthorization(newBarId, owner);

        assertTrue(user.getAuthorizations().containsKey(newBarId));
        assertEquals(Role.OWNER.name(), user.getAuthorizations().get(newBarId));
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"BARTENDER", "CUSTOMER"})
    void authorizeUserForOwnedBar_DoesNotThrow(Role role) {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();

        assertDoesNotThrow(() -> user.authorize(barId, role, person));
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"BARTENDER", "CUSTOMER"})
    void authorizeUserForOwnedBar_AuthorizesUserForBar(Role role) {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();

        user.authorize(barId, role, person);

        String userTwoRole = user.getAuthorizations().get(barId);
        assertEquals(role.name(), userTwoRole);
    }

    @ParameterizedTest
    @MethodSource("provideRolesForChangingRoles")
    void updateAuthorizationOfUser_Successfully_AndDoesNotThrow(Role role, Role newRole) {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();

        user.authorize(barId, role, person);

        assertDoesNotThrow(() -> user.authorize(barId, newRole, person));

        String user2Role = user.getAuthorizations().get(barId);
        assertEquals(newRole.name(), user2Role);
    }

    @Test
    void authorizeUserForOwnedBarAsOwner_Throws() {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> userWithAuthorization.authorize(barId, Role.OWNER, person)
        );
    }

//    @Test
//    void authorizeUserForNotOwnedBarWithNoOwnedBars_Throws() {
//        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
//        Person person = new PersonBuilder("owner").setUser(user).build();
//
//        assertThrows(
//                NotAuthorizedException.class,
//                () -> user.authorize(barIdentification, Role.CUSTOMER, person)
//        );
//    }

//    @Test
//    void authorizeUserForNotOwnedBar_Throws() {
//        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
//        Person person = new PersonBuilder("owner").setUser(user).build();
//        UUID notOwnedBarId = UUID.randomUUID();
//
//        assertThrows(
//                NotAuthorizedException.class,
//                () -> userWithAuthorization.authorize(notOwnedBarId, Role.CUSTOMER, person)
//        );
//    }

//    @Test
//    void authorizeUserForNotOwnedButRelatedBar_Throws() {
//        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
//        Person person = new PersonBuilder("owner").setUser(user).build();
//        userWithAuthorization.authorize(barIdentification, Role.BARTENDER, person);
//
//        assertThrows(
//                NotAuthorizedException.class,
//                () -> user.authorize(barIdentification, Role.CUSTOMER, person)
//        );
//    }

//    @Test
//    void userAuthorizesItself_Throws() {
//        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
//        Person person = new PersonBuilder("owner").setUser(user).build();
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> user.authorize(barIdentification, Role.CUSTOMER, person)
//        );
//    }

    @Test
    void revokeAuthorizedUserAuthorization_RevokesAuthorization() {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();
        user.authorize(barId, Role.CUSTOMER, person);

        userWithAuthorization.revokeUserAuthorization(user, barId);

        assertNull(user.getAuthorizations().get(barId));
    }

    @Test
    void revokeUserAuthorizationWithNoAuthorization_Throws() {
        assertThrows(
                NotAuthorizedException.class,
                () -> user.revokeUserAuthorization(userWithAuthorization, barId)
        );
    }

    @Test
    void revokeUserAuthorizationWithNoAuthorizationLevel_Throws() {
        User user = new User(UUID.randomUUID(), "user", "", "", "", "", "+31612345678", new ArrayList<>());
        Person person = new PersonBuilder("owner").setUser(user).build();
        user.authorize(barId, Role.CUSTOMER, person);

        assertThrows(
                NotAuthorizedException.class,
                () -> user.revokeUserAuthorization(userWithAuthorization, barId)
        );
    }

    @Test
    void revokeOwnUserAuthorization_Throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userWithAuthorization.revokeUserAuthorization(userWithAuthorization, barId)
        );
    }

//    @Test
//    void revokeOwnership_Successfully() {
//        assertDoesNotThrow(() -> userWithAuthorization.revokeOwnership(barIdentification));
//    }
//
//    @Test
//    void revokeOwnershipOfNotOwnedBar_Throws() {
//        assertThrows(
//                NotAuthorizedException.class,
//                () -> user.revokeOwnership(barIdentification)
//        );
//    }
}