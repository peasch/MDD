package com.openclassrooms.mddapi.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Représente une entité JPA correspondant à la table {@code article} dans le schéma {@code MDD}.
 * <p>
 * Cette classe mappe les colonnes de la table en attributs Java et est utilisée
 * pour la persistance des articles dans la base de données.
 * </p>
 *
 * <p>
 * Annotée avec :
 * <ul>
 *     <li>{@link Entity} — pour indiquer qu’il s’agit d’une entité persistante.</li>
 *     <li>{@link Table} — pour définir le nom et le schéma de la table associée.</li>
 *     <li>{@link Data} — de Lombok, pour générer automatiquement getters, setters, equals, hashCode et toString.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque article est associé à un thème et à un auteur, et contient des métadonnées
 * telles que la date de création et la dernière mise à jour.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Entity
@Data
@Table(name = "article", schema = "MDD")
public class Article {

    /** Identifiant unique de l’article (clé primaire auto-incrémentée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** Titre de l’article, limité à 100 caractères. */
    @Column(name = "title", length = 100)
    @Size(max = 100)
    private String title;

    /** Identifiant du thème associé à l’article. */
    @Column(name = "theme_id")
    private int themeId;

    /** Contenu principal de l’article, limité à 2500 caractères. */
    @Column(name = "content", length = 2500)
    @Size(max = 2500)
    private String content;

    /** Identifiant de l’auteur de l’article (référence vers un utilisateur). */
    @Column(name = "user_id")
    private Integer authorId;

    /** Date de création de l’article. */
    @Column(name = "created_at")
    private Date createdAt;

    /** Date de dernière mise à jour de l’article. */
    @Column(name = "updateAt")
    private Date updatedAt;
}
