package com.tungstun.swagger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI31;

@OpenAPI31
@OpenAPIDefinition(
        info = @Info(
                title = "bartap backend Api",
                description = "The backend API for for the bartap project",
                version = "2.0",
                contact = @Contact(
                        name = "Tungstun",
                        url = "https://github.com/tungstun-ict",
                        email = "jort@tungstun.nl"
                )
        ),
        tags = {
                @Tag(name = "Account", description = "Functionality based around the Account and Authentication"),
                @Tag(name = "Bar", description = "Functionality based around the Bar"),
                @Tag(name = "Bill", description = "Functionality based around the Bill"),
                @Tag(name = "Category", description = "Functionality based around the Category"),
                @Tag(name = "Order", description = "Functionality based around the Order"),
                @Tag(name = "Person", description = "Functionality based around the Person"),
                @Tag(name = "Product", description = "Functionality based around the Product"),
                @Tag(name = "Session", description = "Functionality based around the Session")
        }
)

public class SpringDocConfig {
}