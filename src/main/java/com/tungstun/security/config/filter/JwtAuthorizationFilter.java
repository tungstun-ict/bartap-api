package com.tungstun.security.config.filter;

import com.tungstun.security.data.model.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Tries to authorize a user, based on the Bearer token (JWT) from
 * the Authorization header of the incoming request.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final String secret;
    private final String[] ignoredPaths;

    public JwtAuthorizationFilter(String secret, AuthenticationManager authenticationManager) {
        this(secret, authenticationManager, new String[]{});
    }
    public JwtAuthorizationFilter(
            String secret,
            AuthenticationManager authenticationManager,
            String[] ignoredPaths
    ) {
        super(authenticationManager);
        this.secret = secret;
        this.ignoredPaths = ignoredPaths;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
       return Arrays.stream(this.ignoredPaths)
               .anyMatch(path -> path.equals(request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws IOException, ServletException {
        Authentication authentication = this.getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String accessToken = request.getHeader("access_token");
        String tokenType = request.getHeader("token_type");

        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }

        if (!tokenType.equals("bearer")) {
            return null;
        }

        byte[] signingKey = this.secret.getBytes();

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        Jws<Claims> parsedToken = jwtParser
                .parseClaimsJws(accessToken);

        var username = parsedToken
                .getBody()
                .getSubject();

        if (username.isEmpty()) {
            return null;
        }

        UserProfile principal = new UserProfile(username);

        return new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
    }
}