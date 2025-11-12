package com.openclassrooms.mddapi.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Représente les informations de connexion d’un utilisateur.
 * <p>
 * Cette classe est utilisée pour le transfert des données lors d’une
 * tentative d’authentification (login) entre le client et le serveur.
 * </p>
 *
 * <p>
 * Les annotations {@link NotEmpty} garantissent que les champs
 * requis ne soient pas vides lors de la validation du formulaire.
 * </p>
 *
 * <p>
 * Annotée avec {@link Data}, cette classe bénéficie automatiquement
 * des getters, setters, méthodes {@code equals()}, {@code hashCode()} et {@code toString()}.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Data
public class LoginDTO {

    /** Adresse e-mail utilisée comme identifiant de connexion. */
    @NotEmpty
    private String email;

    /** Mot de passe de l’utilisateur. */
    @NotEmpty
    private String password;
}
