package com.openclassrooms.mddapi.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger pour la documentation OpenAPI.
 * <p>
 * Cette classe configure un groupe d’API public pour exposer la documentation
 * de toutes les routes REST disponibles dans l’application.
 * </p>
 *
 * <p>
 * Annotée avec {@link Configuration}, elle est automatiquement détectée par Spring
 * et utilisée par le module <b>springdoc-openapi</b> pour générer la documentation Swagger UI.
 * </p>
 *
 * <p>
 * La documentation Swagger sera accessible depuis :
 * <ul>
 *     <li><code>/swagger-ui.html</code></li>
 *     <li><code>/v3/api-docs</code></li>
 * </ul>
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Crée un groupe public d’API exposant toutes les routes du projet.
     * <p>
     * Ce groupe permet de regrouper sous un même ensemble toutes les routes REST,
     * sans filtrage spécifique, pour la documentation OpenAPI.
     * </p>
     *
     * @return une instance configurée de {@link GroupedOpenApi} pour la documentation Swagger.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
