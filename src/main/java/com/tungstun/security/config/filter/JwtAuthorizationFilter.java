package com.tungstun.security.config.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tungstun.barapi.exceptions.NotAuthenticatedException;
import com.tungstun.security.domain.jwt.JwtValidator;
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
import java.util.List;
import java.util.Optional;

/**
 * Tries to authorize a user, based on the Bearer token (JWT) from
 * the Authorization header of the incoming request.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final String[] ignoredPaths;
    private final JwtValidator validator;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtValidator validator) {
        this(authenticationManager, validator, new String[]{});
    }

    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            JwtValidator validator,
            String[] ignoredPaths
    ) {
        super(authenticationManager);
        this.validator = validator;
        this.ignoredPaths = ignoredPaths;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(this.ignoredPaths)
                .anyMatch(path -> path.equals(request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String tokenType = request.getHeader("token_type");
        String accessToken = request.getHeader("access_token");

        if (accessToken == null || tokenType == null || accessToken.isEmpty() || !tokenType.equalsIgnoreCase("bearer")) {
            chain.doFilter(request, response);
        }

        try {
            DecodedJWT decodedJWT = validator.verifyAccessToken(accessToken);

            Long userId = Optional.ofNullable(decodedJWT.getClaim("client_id").asLong())
                    .orElseThrow(() -> new JWTDecodeException("No client_id in access token"));

            String username = decodedJWT.getSubject();
            List<Authorization> authorizations = decodedJWT.getClaim("authorizations")
                    .asMap()
                    .entrySet()
                    .stream()
                    .map(entry -> new Authorization(entry.getKey(), (String) entry.getValue()))
                    .toList();
            UserProfile principal = new UserProfile(userId, username, authorizations);
            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JWTDecodeException | NotAuthenticatedException ignored) {
            // Continue request without an Authentication bound to the session's request
        }
        chain.doFilter(request, response);
    }
}