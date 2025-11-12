package com.openclassrooms.mddapi.services.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implémentation personnalisée de {@link UserDetails} utilisée par Spring Security
 * pour représenter les informations d’un utilisateur authentifié.
 * <p>
 * Cette classe encapsule les informations de base d’un utilisateur nécessaires
 * au processus d’authentification et d’autorisation :
 * <ul>
 *     <li>Identifiant unique</li>
 *     <li>Nom complet</li>
 *     <li>Adresse e-mail</li>
 * </ul>
 * </p>
 *
 * <p>
 * Annotée avec :
 * <ul>
 *     <li>{@link Builder} — permet la création d’instances via un pattern builder fluide,</li>
 *     <li>{@link AllArgsConstructor} — génère le constructeur complet,</li>
 *     <li>{@link Getter} — génère automatiquement les accesseurs pour les champs.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les méthodes de {@link UserDetails} sont implémentées avec des comportements neutres,
 * à adapter selon les besoins d’authentification (par exemple pour gérer les rôles ou statuts).
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Builder
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    /** Identifiant unique de l’utilisateur. */
    private int id;

    /** Nom complet de l’utilisateur. */
    private String name;

    /** Adresse e-mail de l’utilisateur, utilisée comme identifiant de connexion. */
    private String email;

    /**
     * Retourne les autorités (rôles) associées à l’utilisateur.
     * <p>Cette implémentation renvoie une liste vide par défaut.</p>
     *
     * @return une collection vide d’autorisations.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Retourne le mot de passe de l’utilisateur.
     * <p>Non utilisé dans cette implémentation.</p>
     *
     * @return une chaîne vide.
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * Retourne le nom d’utilisateur utilisé pour l’authentification.
     * <p>Non utilisé dans cette implémentation.</p>
     *
     * @return une chaîne vide.
     */
    @Override
    public String getUsername() {
        return "";
    }

    /**
     * Indique si le compte est expiré.
     * <p>Renvoie {@code true} par défaut (le compte est actif).</p>
     *
     * @return {@code true} si le compte est non expiré.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indique si le compte est verrouillé.
     * <p>Renvoie {@code true} par défaut (le compte n’est pas verrouillé).</p>
     *
     * @return {@code true} si le compte est non verrouillé.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indique si les informations d’identification sont expirées.
     * <p>Renvoie {@code true} par défaut (les identifiants sont valides).</p>
     *
     * @return {@code true} si les identifiants ne sont pas expirés.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indique si le compte est activé.
     * <p>Renvoie {@code true} par défaut (le compte est actif).</p>
     *
     * @return {@code true} si le compte est activé.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
