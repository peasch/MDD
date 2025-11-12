package com.openclassrooms.mddapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un thème sous forme de Data Transfer Object (DTO).
 * <p>
 * Cette classe est utilisée pour transférer les données d’un thème entre les différentes
 * couches de l’application (service, contrôleur, etc.), notamment pour l’exposition via l’API REST.
 * </p>
 *
 * <p>
 * Un thème correspond généralement à une catégorie ou un sujet
 * auquel peuvent être associés plusieurs articles.
 * </p>
 *
 * <p>
 * Les annotations Lombok permettent de générer automatiquement :
 * <ul>
 *     <li>{@link Data} — les getters, setters, equals, hashCode et toString,</li>
 *     <li>{@link Builder} — un pattern builder pour faciliter la création d’instances,</li>
 *     <li>{@link NoArgsConstructor} et {@link AllArgsConstructor} — les constructeurs nécessaires.</li>
 * </ul>
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ThemeDTO {

    /** Identifiant unique du thème. */
    private int id;

    /** Nom du thème. */
    private String name;

    /** Description du thème. */
    private String description;
}
