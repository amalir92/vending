package com.mvp.vending.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiInfo apiInfo() {
        return new ApiInfo("Vending Rest APIs",
                "APIs for Vending application.",
                "1.0",
                "Terms of service",
                new Contact("amali", "amalir92", "amalirathnapriya@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mvp.vending.controller"))
                .build();
    }
}
