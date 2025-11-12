package com.openclassrooms.mddapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Représente un utilisateur sous forme de Data Transfer Object (DTO).
 * <p>
 * Cette classe est utilisée pour transférer les informations d’un utilisateur
 * entre les différentes couches de l’application (entité, service, contrôleur, etc.).
 * </p>
 *
 * <p>
 * Elle contient les informations d’identité, les identifiants de connexion
 * ainsi que la liste des thèmes suivis par l’utilisateur.
 * </p>
 *
 * <p>
 * Annotée avec les annotations Lombok :
 * <ul>
 *     <li>{@link Data} — génère automatiquement les getters, setters, equals, hashCode et toString,</li>
 *     <li>{@link Builder} — permet la création fluide d’instances via un builder,</li>
 *     <li>{@link NoArgsConstructor} et {@link AllArgsConstructor} — fournissent les constructeurs nécessaires.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Cette classe implémente {@link Serializable} afin de permettre la sérialisation des instances,
 * utile notamment pour la mise en cache ou le transport de données via le réseau.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO implements Serializable {

    /** Identifiant unique de la classe pour la sérialisation. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identifiant unique de l’utilisateur. */
    private int id;

    /** Nom complet de l’utilisateur. */
    private String name;

    /** Adresse e-mail de l’utilisateur. */
    private String email;

    /** Nom d’utilisateur (pseudo) visible dans l’application. */
    private String username;

    /** Mot de passe de l’utilisateur. Peut être masqué lors de la sérialisation JSON. */
    @JsonIgnore
    private String password;

    /** Liste des thèmes suivis par l’utilisateur. */
    private List<ThemeDTO> followedThemes;
}
