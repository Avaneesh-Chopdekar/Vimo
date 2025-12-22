package com.avaneesh.vimo_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI().info(
                        new Info()
                                .title("Vimo API")
                                .description("REST API for Vimo, a video streaming application.")
                                .version("1.0.0")
                )
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080/api")
                                        .description("Local Development Server"),
                                new Server().url("https://vimo-backend.onrender.com/api")
                                        .description("Production Server")
                        )
                ) // TODO: Change production server url after deployment
                .tags(
                        List.of(

                        )
                )
                .addSecurityItem(
                        new SecurityRequirement().addList("bearerAuth")
                )
                .components(
                        new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
