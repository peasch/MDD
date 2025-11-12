package com.openclassrooms.mddapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de l'API OpenAPI (Swagger) pour la documentation automatique du projet.
 * <p>
 * Cette classe définit la configuration de base de la documentation OpenAPI, notamment :
 * <ul>
 *     <li>Les informations générales de l'API (titre, description, version, licence, contact).</li>
 *     <li>Le schéma de sécurité utilisé (ici un token Bearer JWT).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Annotée avec {@link Configuration}, elle est automatiquement détectée et enregistrée
 * comme configuration Spring lors du démarrage de l'application.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Crée et configure l'instance principale d'OpenAPI.
     * <p>
     * Elle ajoute :
     * <ul>
     *     <li>Un schéma de sécurité basé sur un token Bearer JWT.</li>
     *     <li>Les métadonnées de l’API (titre, description, version, contact, licence).</li>
     * </ul>
     * </p>
     *
     * @return une instance configurée de {@link OpenAPI} utilisée par Swagger UI.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes(
                        "Bearer Authentication", createAPIKeyScheme()))
                .info(new Info()
                        .title("My REST API")
                        .description("Some custom description of API.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Sallo Szrajbman")
                                .email("www.baeldung.com")
                                .url("salloszraj@gmail.com"))
                        .license(new License()
                                .name("License of API")
                                .url("API license URL")));
    }

    /**
     * Crée la définition du schéma de sécurité JWT pour OpenAPI.
     * <p>
     * Ce schéma indique que l’authentification se fait par un jeton Bearer
     * transmis dans l’en-tête HTTP « Authorization ».
     * </p>
     *
     * @return un objet {@link SecurityScheme} configuré pour l’authentification JWT.
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("Bearer");
    }
}
