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

    public String generate(User user) {
        return generateJWT(user);
    }

    private String generateJWT(User user) {
        byte[] signingKey = CREDENTIALS.jwtSecret.getBytes();
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setHeaderParam("type", "JWT")
                .setIssuer("com-tungstun-bar-api")
                .setAudience("com-tungstun-bar-api")
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + CREDENTIALS.jwtExpirationInMs))
                .compact();
    }

    private List<String> extractUserRoles(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
