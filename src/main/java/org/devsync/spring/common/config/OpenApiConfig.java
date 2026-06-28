package org.devsync.spring.common.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI devSyncOpenAPI() {
        return new OpenAPI().tags(List.of(
                        new Tag().name("Authentication"),
                        new Tag().name("Users"),
                        new Tag().name("Workspaces"),
                        new Tag().name("Projects"),
                        new Tag().name("Labels"),
                        new Tag().name("Issues"),
                        new Tag().name("Comments"),
                        new Tag().name("Issue Labels"),
                        new Tag().name("Watchers"),
                        new Tag().name("Notifications"),
                        new Tag().name("Attachments"),
                        new Tag().name("Dashboard"),
                        new Tag().name("Activities")
                ))
                .info(
                        new Info()
                                .title("DevSync")
                                .version("v1")
                                .description("Collaborative Workspace and Issue Tracking Platform")
                                .contact(new Contact().email("skilledsapien007@gmail.com")).license(new License().name("MIT"))

                ).addSecurityItem(
                        new SecurityRequirement().addList(SECURITY_SCHEME_NAME)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
