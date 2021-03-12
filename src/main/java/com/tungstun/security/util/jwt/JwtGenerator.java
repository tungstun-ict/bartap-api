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
    private final JwtCredentials CREDENTIALS;

    public JwtGenerator(JwtCredentials jwtCredentials) {
        this.CREDENTIALS = jwtCredentials;
    }

    public String generateAccessToken(User user) {
        return createAccessToken(user.getUsername());
    }

    public String refreshAccessTokenFromAccessToken(String accessToken) {
        String username = new JwtValidator(CREDENTIALS).extractUsernameFromExpiredAccessToken(accessToken);
        return createAccessToken(username);
    }

    public String generateRefreshToken() {
        return createRefreshToken();
    }

    private String createAccessToken(String username) {
        JwtBuilder builder = createBuilderSetup();
        return builder.signWith(Keys.hmacShaKeyFor(CREDENTIALS.jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + CREDENTIALS.jwtExpirationInMs))
                .setSubject(username)
                .compact();
    }

    private String createRefreshToken() {
        JwtBuilder builder = createBuilderSetup();
        return builder.signWith(Keys.hmacShaKeyFor(CREDENTIALS.jwtRefreshSecret.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + CREDENTIALS.jwtRefreshExpirationInMs))
                .compact();
    }

    private JwtBuilder createBuilderSetup(){
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setIssuer(CREDENTIALS.jwtIssuer)
                .setAudience(CREDENTIALS.jwtAudience);
    }

//    private List<String> extractUserRoles(User user) {
//        return user.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//    }
}
