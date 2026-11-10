package ru.ulstu.is.server.security;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import ru.ulstu.is.server.configuration.Constants;

@Configuration
@SecurityScheme(name = SpringdocsConfiguration.SCHEME_NAME, type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SpringdocsConfiguration {
    public static final String SCHEME_NAME = "bearerAuth";

    @Bean
    OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> {
            final SecurityRequirement securityRequirement = new SecurityRequirement().addList(SCHEME_NAME);

            openApi.getPaths().forEach((path, pathItem) -> {
                if (path.startsWith(Constants.API_URL) || path.endsWith(Constants.WHO_AM_I)) {
                    pathItem.readOperations().forEach(operation -> operation.addSecurityItem(securityRequirement));
                }
            });
        };
    }
}
