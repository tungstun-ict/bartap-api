package com.tungstun.security.presentation.filter;

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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tries to authorize a user, based on the Bearer token (JWT) from
 * the Authorization header of the incoming request.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final String secret;

    public JwtAuthorizationFilter(
            String secret,
            AuthenticationManager authenticationManager
    ) {
        super(authenticationManager);

        this.secret = secret;
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
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            return null;
        }

        if (!token.startsWith("Bearer ")) {
            return null;
        }

        byte[] signingKey = this.secret.getBytes();

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        Jws<Claims> parsedToken = jwtParser
                .parseClaimsJws(token.replace("Bearer ", ""));

        var username = parsedToken
                .getBody()
                .getSubject();

        Map<Long, String> barAuthorities = new LinkedHashMap<>();
        for (var entry : ((LinkedHashMap<?, ?>) parsedToken.getBody().get("barRoles")).entrySet()) {
            barAuthorities.put(Long.valueOf((String) entry.getKey()), (String) entry.getValue());
        }

        if (username.isEmpty()) {
            return null;
        }

        UserProfile principal = new UserProfile(username, barAuthorities);

        return new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
    }
}