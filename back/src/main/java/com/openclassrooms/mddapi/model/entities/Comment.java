package com.openclassrooms.mddapi.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Représente un commentaire en base de données.
 * <p>
 * Cette classe est une entité JPA mappée à la table {@code comment} du schéma {@code MDD}.
 * Elle contient les informations relatives à un commentaire d’article publié par un utilisateur.
 * </p>
 *
 * <p>
 * Annotée avec :
 * <ul>
 *     <li>{@link Entity} — indique qu’il s’agit d’une entité persistante gérée par JPA,</li>
 *     <li>{@link Table} — précise le nom et le schéma de la table associée,</li>
 *     <li>{@link Data} — de Lombok, pour générer automatiquement getters, setters, equals, hashCode et toString.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque commentaire est lié à un utilisateur (auteur) et à un article.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Entity
@Data
@Table(name = "comment", schema = "MDD")
public class Comment implements Serializable {

    /** Identifiant unique du commentaire (clé primaire auto-incrémentée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** Contenu textuel du commentaire. */
    @Column(name = "content")
    private String content;

    /** Identifiant de l’utilisateur ayant publié le commentaire. */
    @Column(name = "user_id")
    private int authorId;

    /** Identifiant de l’article sur lequel le commentaire a été posté. */
    @Column(name = "article_id")
    private int articleId;

    /** Date de création du commentaire. */
    @Column(name = "created_at")
    private Date createdAt;
}
