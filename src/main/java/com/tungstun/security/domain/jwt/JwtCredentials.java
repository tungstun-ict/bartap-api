package com.tungstun.security.domain.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.tungstun.bartap.security.jwt")
public class JwtCredentials {
    private String jwtSecret;
    private Long jwtExpirationInMs;
    private Long jwtRefreshExpirationInMs;
    private Long jwtPersonConnectExpirationInMs;
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

    public Long getJwtPersonConnectExpirationInMs() {
        return jwtPersonConnectExpirationInMs;
    }

    public void setJwtPersonConnectExpirationInMs(Long jwtPersonConnectExpirationInMs) {
        this.jwtPersonConnectExpirationInMs = jwtPersonConnectExpirationInMs;
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
}
