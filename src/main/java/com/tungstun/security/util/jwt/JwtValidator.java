package com.tungstun.security.util.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private final JwtCredentials credentials;

    public JwtValidator(JwtCredentials credentials) {
        this.credentials = credentials;
    }

    public void validateAccessJwt(String jwt) {
        validateJwt(jwt, credentials.jwtSecret);
    }

    public void validateRefreshJwt(String jwt) {
        validateJwt(jwt, credentials.jwtRefreshSecret);
    }

    public void validateJwt(String jwt, String secret) {
        if (jwt == null || jwt.isEmpty()) throw new JwtException("Empty JWT");
        JwtParser jwtParser = createParser(secret);
        tryToParseClaimsFromToken(jwtParser, jwt);
    }

    private JwtParser createParser(String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build();
    }

    private Jws<Claims> tryToParseClaimsFromToken(JwtParser parser, String jwt) {
        Jws<Claims> parsedToken = null;
        try {
            parsedToken = parser.parseClaimsJws(jwt);
            validateTokenBody(parsedToken);

        }catch (Exception e) {
            if (!(e instanceof ExpiredJwtException || e instanceof PrematureJwtException)) throw new JwtException("Invalid jwt");
        }
        return parsedToken;
    }

    private void validateTokenBody(Jws<Claims> claims) {
        if (claims != null &&
                !claims.getBody().getAudience().equals(credentials.jwtAudience) &&
                !claims.getBody().getIssuer().equals(credentials.jwtIssuer))
            throw new JwtException("Invalid JWT");
    }

    protected String extractUsernameFromExpiredAccessToken(String accessToken) {
        JwtParser parser = createParser(credentials.jwtSecret);
        String username;
        try {
            username = parser.parseClaimsJws(accessToken).getBody().getSubject();
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) username = ((ExpiredJwtException) e).getClaims().getSubject();
            else throw new JwtException("Invalid token, please login");
        }
        return username;
    }
}
