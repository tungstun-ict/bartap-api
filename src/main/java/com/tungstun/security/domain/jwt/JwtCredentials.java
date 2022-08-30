package com.tungstun.security.domain.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ConfigurationProperties(prefix = "com.tungstun.bartap.security.jwt")
public class JwtCredentials {
    private String jwtSecret;
    private Long jwtExpirationInMs;
    private Long jwtRefreshExpirationInMs;
    private String[] jwtAudience;
    private String jwtIssuer;

    public Algorithm algorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }

    public void setJwtExpirationInMs(Long jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public Long getJwtRefreshExpirationInMs() {
        return jwtRefreshExpirationInMs;
    }

    public void setJwtRefreshExpirationInMs(Long jwtRefreshExpirationInMs) {
        this.jwtRefreshExpirationInMs = jwtRefreshExpirationInMs;
    }

    public String[] getJwtAudience() {
        return jwtAudience;
    }

    public void setJwtAudience(String[] jwtAudience) {
        this.jwtAudience = jwtAudience;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    public String toString() {
        return "JwtCredentials{" +
                "jwtSecret='" + jwtSecret + '\'' +
                ", jwtExpirationInMs=" + jwtExpirationInMs +
                ", jwtRefreshExpirationInMs=" + jwtRefreshExpirationInMs +
                ", jwtAudience=" + Arrays.toString(jwtAudience) +
                ", jwtIssuer='" + jwtIssuer + '\'' +
                '}';
    }
}