package com.openclassrooms.mddapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Représente un article sous forme de Data Transfer Object (DTO).
 * <p>
 * Cette classe est utilisée pour transférer les données des articles
 * entre les différentes couches de l’application (par exemple entre la couche service et le contrôleur).
 * </p>
 *
 * <p>
 * Annotée avec les annotations Lombok :
 * <ul>
 *     <li>{@link Data} — génère automatiquement les getters, setters, equals, hashCode et toString.</li>
 *     <li>{@link Builder} — permet la création d’instances via un pattern builder fluide.</li>
 *     <li>{@link NoArgsConstructor} et {@link AllArgsConstructor} — génèrent les constructeurs nécessaires.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Cette classe implémente {@link Serializable} afin de pouvoir être facilement
 * transmise ou stockée sous forme binaire (utile pour la mise en cache, la session ou la communication réseau).
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleDTO implements Serializable {

    /** Identifiant unique de la classe pour la sérialisation. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identifiant unique de l’article. */
    private int id;

    /** Titre de l’article. */
    private String title;

    /** Identifiant du thème auquel l’article est associé. */
    private int themeId;

    /** Contenu textuel de l’article. */
    private String content;

    /** Identifiant de l’auteur de l’article. */
    private int authorId;

    /** Nom d’utilisateur (username) de l’auteur. */
    private String authorUsername;

    /** Date de création de l’article. */
    private Date createdAt;

    /** Date de dernière mise à jour de l’article. */
    private Date updatedAt;
}
