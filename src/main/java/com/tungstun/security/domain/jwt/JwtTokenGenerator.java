package com.tungstun.security.domain.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.tungstun.security.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenGenerator {
    private final JwtCredentials credentials;

    public JwtTokenGenerator(JwtCredentials credentials) {
        this.credentials = credentials;
    }

    public String createAccessToken(User user) {
        try {
            Map<String, String> authorizations = user.getAuthorizations()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(k -> String.valueOf(k.getKey()), Map.Entry::getValue));
            return JWT.create()
                    .withIssuer(credentials.getJwtIssuer())
                    .withAudience(credentials.getJwtAudience())
                    .withExpiresAt(new Date(System.currentTimeMillis() + credentials.getJwtExpirationInMs()))
                    .withSubject(user.getUsername())
                    .withClaim("client_id", user.getId().toString())
                    .withClaim("authorizations", authorizations)
                    .sign(credentials.algorithm());
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Exception occurred during the creation of an access token", e);
        }
    }

    public String createRefreshToken() {
        try {
            return JWT.create()
                    .withIssuer(credentials.getJwtIssuer())
                    .withAudience(credentials.getJwtAudience())
                    .withExpiresAt(new Date(System.currentTimeMillis() + credentials.getJwtRefreshExpirationInMs()))
                    .sign(credentials.algorithm());
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Exception occurred during the creation of th refresh token", e);
        }
    }

    public String createPersonConnectionToken(UUID barId, UUID personId) {
        try {
            return JWT.create()
                    .withIssuer(credentials.getJwtIssuer())
                    .withAudience(credentials.getJwtAudience())
                    .withExpiresAt(new Date(System.currentTimeMillis() + credentials.getJwtPersonConnectExpirationInMs()))
                    .withClaim("bar_id", barId.toString())
                    .withClaim("person_id", personId.toString())
                    .sign(credentials.algorithm());
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Exception occurred during the creation of the person connection token", e);
        }
    }
}
