package io.cartel.noord.brabant.api.configs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${app.description}") String description,
            @Value("${app.version}") String version,
            @Value("${app.source.url}") String sourceURL
    ) {
        var info = new Info()
                .title("Diff API")
                .version(version)
                .description(description);

        var externalDocs = new ExternalDocumentation()
                .description("Source Code")
                .url(sourceURL);

        return new OpenAPI()
                .info(info)
                .externalDocs(externalDocs);
    }
}
