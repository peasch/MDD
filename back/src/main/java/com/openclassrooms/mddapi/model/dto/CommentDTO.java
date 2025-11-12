package com.openclassrooms.mddapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Représente un commentaire sous forme de Data Transfer Object (DTO).
 * <p>
 * Cette classe est utilisée pour transporter les données d’un commentaire
 * entre les différentes couches de l’application (contrôleur, service, dépôt, etc.).
 * </p>
 *
 * <p>
 * Elle utilise les annotations Lombok afin de réduire le code boilerplate :
 * <ul>
 *     <li>{@link Data} — génère automatiquement les getters, setters, equals, hashCode et toString.</li>
 *     <li>{@link Builder} — permet de construire facilement des instances avec le pattern builder.</li>
 *     <li>{@link NoArgsConstructor} et {@link AllArgsConstructor} — fournissent les constructeurs nécessaires.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Le DTO est généralement utilisé pour exposer uniquement les données nécessaires
 * à la communication entre la couche back-end et le front-end, sans inclure la logique métier.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommentDTO {

    /** Identifiant unique du commentaire. */
    private int id;

    /** Contenu textuel du commentaire. */
    private String content;

    /** Identifiant de l’auteur ayant publié le commentaire. */
    private int authorId;

    /** Identifiant de l’article auquel le commentaire est associé. */
    private int articleId;

    /** Date de création du commentaire. */
    private Date createdAt;
}
