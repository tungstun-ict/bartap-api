package com.tungstun.security.util.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtCredentials {
    @Value("${security.jwt.secret}")
    protected String jwtSecret;

    @Value("${security.jwt.expiration-in-ms}")
    protected Integer jwtExpirationInMs;
}
