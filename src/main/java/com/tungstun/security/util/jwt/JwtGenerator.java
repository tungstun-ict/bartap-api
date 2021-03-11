package com.tungstun.security.util.jwt;

import com.tungstun.security.data.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtGenerator {
    private JwtCredentials CREDENTIALS;

    public JwtGenerator(JwtCredentials jwtCredentials) {
        this.CREDENTIALS = jwtCredentials;
    }

    public String generateAccessToken(User user) {
        return generateJWT(user.getUsername(), CREDENTIALS.jwtSecret, CREDENTIALS.jwtExpirationInMs);
    }

    public String refreshAccessTokenFromRefreshToken(String accessToken) {
        String username = new JwtValidator(CREDENTIALS).extractUsernameFromRefreshToken(accessToken);
        return generateJWT(username, CREDENTIALS.jwtSecret, CREDENTIALS.jwtExpirationInMs);
    }

    public String generateRefreshToken(User user) {
        return generateJWT(user.getUsername(), CREDENTIALS.jwtRefreshSecret, CREDENTIALS.jwtRefreshExpirationInMs);
    }

    private String generateJWT(String username, String signingKey, Integer expirationTimeInMs) {
        byte[] key = signingKey.getBytes();
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .setHeaderParam("type", "JWT")
                .setIssuer(CREDENTIALS.jwtIssuer)
                .setAudience(CREDENTIALS.jwtAudience)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMs))
                .compact();
    }

    private List<String> extractUserRoles(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
