package com.tungstun.security.config;

import com.tungstun.security.presentation.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

public class BarApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
    public final static String LOGIN_PATH = "/api/login";
    public final static String REGISTER_PATH = "/api/register";
    private final static String[] SWAGGER_PATHS = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // -- Simple swagger redirect URI
            "/swagger"
    };

    @Value("${security.jwt.secret}")
    private String jwtSecret;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, REGISTER_PATH).permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_PATH).permitAll()
                .antMatchers(SWAGGER_PATHS).permitAll()
                .anyRequest().authenticated()
                .and()
            .addFilter(new JwtAuthorizationFilter(this.jwtSecret, this.authenticationManager()))
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
