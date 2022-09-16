package com.tungstun.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .specVersion(SpecVersion.V31)
                .info(new Info()
                        .title("bartap backend API")
                        .description("The backend API for for the bartap project")
                        .version("2.0")
                        .contact(new Contact()
                                .name("Tungstun")
                                .url("https://github.com/tungstun-ict")
                                .email("jort@tungstun.nl"))
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.APIKEY)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)

                                )
                )
                .tags(List.of(
                        new Tag().name("Account").description("Functionality based around the Account and Authentication"),
                        new Tag().name("Bar").description("Functionality based around the Bar"),
                        new Tag().name("Bill").description("Functionality based around the Bill"),
                        new Tag().name("Category").description("Functionality based around the Category"),
                        new Tag().name("Order").description("Functionality based around the Order"),
                        new Tag().name("Person").description("Functionality based around the Person"),
                        new Tag().name("Product").description("Functionality based around the Product"),
                        new Tag().name("Session").description("Functionality based around the Session"),
                        new Tag().name("Statistics").description("Functionality based around the Bar, Account and Customer statistics")
                ));
    }
}
