package com.tungstun.security.config;

import com.tungstun.security.config.filter.JwtAuthorizationFilter;
import com.tungstun.security.domain.jwt.JwtValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

public class BarApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PATH = "/api/authenticate";
    private static final String LOGIN_REFRESH_PATH = "/api/authenticate/refresh";
    private static final String REGISTER_PATH = "/api/register";
    private static final String[] SWAGGER_PATHS = {
            // -- Swagger UI v3 (OpenAPI)
            "/swagger-ui/**",
            "/v3/api-docs/**",
            // -- Simple swagger redirect URI
            "/swagger"
    };

    @Autowired
    private JwtValidator validator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, new String[]{
                        REGISTER_PATH,
                        LOGIN_PATH,
                        LOGIN_REFRESH_PATH
                }).permitAll()
                .antMatchers(SWAGGER_PATHS).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthorizationFilter(
                        authenticationManager(),
                        validator,
                        ArrayUtils.addAll(
                                SWAGGER_PATHS,
                                LOGIN_PATH,
                                REGISTER_PATH,
                                LOGIN_REFRESH_PATH
                        )
                ))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
