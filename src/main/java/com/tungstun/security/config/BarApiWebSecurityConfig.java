package com.tungstun.security.config;

import com.tungstun.security.config.filter.JwtAuthorizationFilter;
import com.tungstun.security.domain.jwt.JwtValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class BarApiWebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static final String REGISTER_PATH = "/api/account/register";
    private static final String LOGIN_PATH = "/api/authenticate";
    private static final String LOGIN_REFRESH_PATH = "/api/authenticate/refresh";
    private static final String[] SWAGGER_PATHS = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
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
        http.cors().and()
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers(ArrayUtils.addAll(SWAGGER_PATHS,
                        REGISTER_PATH,
                        LOGIN_PATH,
                        LOGIN_REFRESH_PATH
                )).permitAll()
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
