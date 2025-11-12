package com.openclassrooms.mddapi.model.entities;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Représente un thème en base de données.
 * <p>
 * Cette classe est une entité JPA mappée à la table {@code theme} du schéma {@code MDD}.
 * Elle correspond à un sujet ou une catégorie auxquels des articles peuvent être associés.
 * </p>
 *
 * <p>
 * Annotée avec :
 * <ul>
 *     <li>{@link Entity} — indique qu’il s’agit d’une entité persistante gérée par JPA,</li>
 *     <li>{@link Table} — définit le nom et le schéma de la table correspondante,</li>
 *     <li>{@link Data} — de Lombok, pour générer automatiquement les getters, setters, equals, hashCode et toString.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Un thème peut être suivi par plusieurs utilisateurs et contenir plusieurs articles.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Entity
@Data
@Table(name = "theme", schema = "MDD")
public class Theme implements Serializable {

    /** Identifiant unique du thème (clé primaire auto-incrémentée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** Nom du thème. */
    @Column(name = "name")
    private String name;

    /** Description du thème. */
    @Column(name = "description")
    private String description;
}
