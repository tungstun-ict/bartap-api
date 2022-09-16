package com.tungstun.security.domain.domain.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.barapi.domain.person.Person;
import com.tungstun.security.domain.jwt.JwtCredentials;
import com.tungstun.security.domain.jwt.JwtTokenGenerator;
import com.tungstun.security.domain.jwt.JwtValidator;
import com.tungstun.security.domain.user.Authorization;
import com.tungstun.security.domain.user.Role;
import com.tungstun.security.domain.user.User;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(JwtCredentials.class)
@TestPropertySource("classpath:application.properties")
class JwtTokenGeneratorTest {
    @Autowired
    private JwtCredentials jwtCredentials;
    private JwtTokenGenerator tokenGenerator;
    private JwtValidator jwtValidator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new JwtTokenGenerator(jwtCredentials);
        jwtValidator = new JwtValidator(jwtCredentials);
    }

    @SuppressWarnings("java:S2925")
    @Test
    void createAccessToken_ContainsCorrectValues() throws IllegalAccessException {
        User user = new User(
                UUID.randomUUID(),
                "userId",
                "password",
                "mail@mail.com",
                "first",
                "last",
                "+31612345678", new ArrayList<>(List.of(new Authorization(UUID.randomUUID(), UUID.randomUUID(), Role.OWNER, new Person()))));
        FieldUtils.writeField(user, "id", UUID.randomUUID(), true);
        long before = ZonedDateTime.now().minusSeconds(1).toInstant().toEpochMilli();
        long after = ZonedDateTime.now().plusSeconds(1).toInstant().toEpochMilli() + jwtCredentials.getJwtExpirationInMs();

        String token = tokenGenerator.createAccessToken(user);

        DecodedJWT decodedJWT = jwtValidator.verifyAccessToken(token);
        assertTrue(decodedJWT.getExpiresAt().after(new Date(before)));
        assertTrue(decodedJWT.getExpiresAt().before(new Date(after)));
        assertEquals(List.of(jwtCredentials.getJwtAudience()), decodedJWT.getAudience());
        assertEquals(jwtCredentials.getJwtIssuer(), decodedJWT.getIssuer());
        assertEquals(user.getId(), UUID.fromString(decodedJWT.getClaim("client_id").asString()));
        assertEquals(user.getUsername(), decodedJWT.getSubject());
        Map<UUID, String> authorizations = decodedJWT.getClaim("authorizations")
                .asMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(k -> UUID.fromString(k.getKey()), v -> v.getValue().toString()));
        assertEquals(user.getAuthorizations(), authorizations);
    }

    @SuppressWarnings("java:S2925")
    @Test
    void createRefreshToken_ContainsCorrectValues() {
        long before = ZonedDateTime.now().minusSeconds(1).toInstant().toEpochMilli();

        String token = tokenGenerator.createRefreshToken();

        long after = ZonedDateTime.now().plusSeconds(1).toInstant().toEpochMilli() + jwtCredentials.getJwtRefreshExpirationInMs();
        DecodedJWT decodedJWT = jwtValidator.verifyToken(token);
        assertTrue(decodedJWT.getExpiresAt().after(new Date(before)));
        assertTrue(decodedJWT.getExpiresAt().before(new Date(after)));
        assertEquals(List.of(jwtCredentials.getJwtAudience()), decodedJWT.getAudience());
        assertEquals(jwtCredentials.getJwtIssuer(), decodedJWT.getIssuer());
    }

    @SuppressWarnings("java:S2925")
    @Test
    void createConnectToken_ContainsCorrectValues() {
        UUID barId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        long before = ZonedDateTime.now().minusSeconds(1).toInstant().toEpochMilli();

        String token = tokenGenerator.createPersonConnectionToken(barId, personId);

        long after = ZonedDateTime.now().plusSeconds(1).toInstant().toEpochMilli() + jwtCredentials.getJwtRefreshExpirationInMs();
        DecodedJWT decodedJWT = jwtValidator.verifyToken(token);
        assertTrue(decodedJWT.getExpiresAt().after(new Date(before)));
        assertTrue(decodedJWT.getExpiresAt().before(new Date(after)));
        assertEquals(List.of(jwtCredentials.getJwtAudience()), decodedJWT.getAudience());
        assertEquals(jwtCredentials.getJwtIssuer(), decodedJWT.getIssuer());
        assertEquals(barId, UUID.fromString(decodedJWT.getClaim("bar_id").asString()));
        assertEquals(personId, UUID.fromString(decodedJWT.getClaim("person_id").asString()));
    }
}
