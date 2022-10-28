package com.tungstun.barapi.domain.person;

import com.tungstun.security.domain.user.Role;
import com.tungstun.security.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    @DisplayName("Connect user to person")
    void connectUserToPerson() {
        User user = new User(UUID.randomUUID(),"name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>());
        Person person = new PersonBuilder("person").build();
        UUID barId = UUID.randomUUID();
        person.connectUser(user, barId);

        assertEquals(user, person.getUser());
        assertEquals(1, user.getAuthorizations().size());
        assertEquals(Role.CUSTOMER.toString(), user.getAuthorizations().get(barId));
    }

    @Test
    @DisplayName("Connect user to person that i already connected to a user")
    void connectUserToPersonAlreadyConnected() {
        User user = new User(UUID.randomUUID(),"name2", "", "", "", "mail@mail.mm", "+31612345876", new ArrayList<>());
        Person person = new PersonBuilder("person").setUser(user).build();

        assertThrows(
                IllegalStateException.class,
                () -> person.connectUser(user, UUID.randomUUID())
        );
    }
}
