package com.openclassrooms.mddapi.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Représente un utilisateur en base de données.
 * <p>
 * Cette classe est une entité JPA mappée à la table {@code user} du schéma {@code MDD}.
 * Elle contient les informations principales d’un utilisateur, notamment ses identifiants
 * de connexion, ses données personnelles et les thèmes qu’il suit.
 * </p>
 *
 * <p>
 * Annotée avec :
 * <ul>
 *     <li>{@link Entity} — indique qu’il s’agit d’une entité persistante gérée par JPA,</li>
 *     <li>{@link Table} — définit le nom et le schéma de la table associée,</li>
 *     <li>{@link Data} — de Lombok, pour générer automatiquement les getters, setters, equals, hashCode et toString,</li>
 *     <li>{@link JsonIgnore} — pour masquer le mot de passe lors de la sérialisation JSON.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque utilisateur peut suivre plusieurs thèmes via une relation {@link ManyToMany}
 * matérialisée par la table de jointure {@code follow}.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Entity
@Data
@Table(name = "user", schema = "MDD")
public class User implements Serializable {

    /** Identifiant unique de l’utilisateur (clé primaire auto-incrémentée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** Nom complet de l’utilisateur. */
    @Column(name = "name")
    private String name;

    /** Adresse e-mail de l’utilisateur, utilisée pour l’authentification. */
    @Column(name = "email")
    private String email;

    /** Nom d’utilisateur (pseudo) visible dans l’application. */
    @Column(name = "username")
    private String username;

    /** Mot de passe de l’utilisateur (non exposé en JSON). */
    @JsonIgnore
    @Column(name = "password")
    private String password;

    /**
     * Liste des thèmes suivis par l’utilisateur.
     * <p>
     * Relation de type {@link ManyToMany} entre {@link User} et {@link Theme}.
     * Elle est mappée via la table de jointure {@code follow}, qui lie les colonnes :
     * <ul>
     *     <li>{@code user_id} — référence à l’utilisateur,</li>
     *     <li>{@code theme_id} — référence au thème suivi.</li>
     * </ul>
     * </p>
     */
    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private List<Theme> followedThemes;
}
