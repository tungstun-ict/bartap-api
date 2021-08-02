package com.tungstun.security.util.jwt;

import com.tungstun.security.data.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {
    private final JwtCredentials credentials;

    public JwtGenerator(JwtCredentials jwtCredentials) {
        this.credentials = jwtCredentials;
    }

    public String generateAccessToken(User user) {
        return createAccessToken(user.getUsername());
    }

    public String refreshAccessTokenFromAccessToken(String accessToken) {
        String username = new JwtValidator(credentials).extractUsernameFromExpiredAccessToken(accessToken);
        return createAccessToken(username);
    }

    public String generateRefreshToken() {
        return createRefreshToken();
    }

    private String createAccessToken(String username) {
        JwtBuilder builder = createBuilderSetup();
        return builder.signWith(Keys.hmacShaKeyFor(credentials.jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + credentials.jwtExpirationInMs))
                .setSubject(username)
                .compact();
    }

    private String createRefreshToken() {
        JwtBuilder builder = createBuilderSetup();
        return builder.signWith(Keys.hmacShaKeyFor(credentials.jwtRefreshSecret.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + credentials.jwtRefreshExpirationInMs))
                .compact();
    }

    private JwtBuilder createBuilderSetup(){
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setIssuer(credentials.jwtIssuer)
                .setAudience(credentials.jwtAudience);
    }
}
