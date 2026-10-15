package com.novalabs.digitalbanking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI digitalBankingOpenAPI() {
        final String bearerScheme = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Digital Banking API")
                        .version("v1")
                        .description("""
                                REST API for the Digital Banking application.
                                Auth endpoints issue JWT access tokens. All other API operations
                                require a valid Bearer token and applicable role permissions.
                                """)
                        .contact(new Contact()
                                .name("Digital Banking Engineering")))
                .addSecurityItem(new SecurityRequirement().addList(bearerScheme))
                .components(new Components()
                        .addSecuritySchemes(bearerScheme,
                                new SecurityScheme()
                                        .name(bearerScheme)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
