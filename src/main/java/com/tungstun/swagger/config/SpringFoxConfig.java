//package com.tungstun.swagger.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Collections;
//
//@Configuration
//@EnableSwagger2
//@EnableOpenApi
//public class SpringFoxConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .paths(PathSelectors.ant("/api/**"))
//				.apis(RequestHandlerSelectors.basePackage("com.tungstun"))
//                .build()
//                .apiInfo(apiDetails());
//    }
//
//    private ApiInfo apiDetails() {
//        return new ApiInfo(
//                "Bar application API",
//                "API for back-end side of the bar application project",
//                "1.0",
//                "To Be Added TOS link", //todo
//                new Contact(
//                        "Jona Leeflang",
//                        "https://github.com/ChromaChroma",
//                        "jona.beer@gmail.com"
//                ),
//                "To Be Added License type",  //todo
//                "To Be added License Url", //todo
//                Collections.emptyList()
//        );
//    }
//
//}