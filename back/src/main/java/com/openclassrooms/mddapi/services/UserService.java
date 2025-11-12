package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.UserDTO;

/**
 * Service définissant les opérations métier liées à la gestion des utilisateurs.
 * <p>
 * Cette interface regroupe l’ensemble des fonctionnalités permettant de :
 * <ul>
 *     <li>Créer, mettre à jour et supprimer un utilisateur,</li>
 *     <li>Récupérer un utilisateur par son identifiant, son e-mail ou son nom d’utilisateur,</li>
 *     <li>Vérifier l’existence d’un utilisateur,</li>
 *     <li>Gérer les thèmes suivis par un utilisateur (ajout ou suppression).</li>
 * </ul>
 * </p>
 *
 * <p>
 * L’implémentation par défaut de cette interface est
 * {@link com.openclassrooms.mddapi.services.impl.UserServiceImpl}.
 * </p>
 *
 * @see com.openclassrooms.mddapi.model.dto.UserDTO
 * @see com.openclassrooms.mddapi.model.entities.User
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface UserService {

    /**
     * Recherche un utilisateur à partir de son adresse e-mail.
     *
     * @param email l’adresse e-mail recherchée.
     * @return le {@link UserDTO} correspondant, ou {@code null} si aucun utilisateur n’existe avec cet e-mail.
     */
    UserDTO getUserByEmail(String email);

    /**
     * Recherche un utilisateur à partir de son nom d’utilisateur (username).
     *
     * @param username le nom d’utilisateur recherché.
     * @return le {@link UserDTO} correspondant, ou {@code null} si aucun utilisateur n’existe avec ce nom.
     */
    UserDTO getUserByUsername(String username);

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param userDto l’objet {@link UserDTO} représentant le nouvel utilisateur.
     * @return le {@link UserDTO} sauvegardé (sans le mot de passe).
     */
    UserDTO saveUser(UserDTO userDto);

    /**
     * Récupère un utilisateur à partir de son identifiant unique.
     *
     * @param id l’identifiant de l’utilisateur.
     * @return le {@link UserDTO} correspondant.
     */
    UserDTO getUserById(int id);

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id l’identifiant de l’utilisateur à supprimer.
     */
    void deleteUserById(int id);

    /**
     * Met à jour les informations d’un utilisateur existant.
     *
     * @param userDto l’objet {@link UserDTO} contenant les nouvelles informations.
     * @return le {@link UserDTO} mis à jour (sans le mot de passe).
     */
    UserDTO updateUser(UserDTO userDto);

    /**
     * Vérifie si un utilisateur existe à partir de son identifiant.
     *
     * @param id l’identifiant à vérifier.
     * @return {@code true} si l’utilisateur existe, sinon {@code false}.
     */
    boolean checkId(Integer id);

    /**
     * Ajoute un thème à la liste des thèmes suivis par un utilisateur.
     *
     * @param user    l’utilisateur concerné.
     * @param themeId l’identifiant du thème à suivre.
     * @return le {@link UserDTO} mis à jour.
     */
    UserDTO addThemeToFollowed(UserDTO user, int themeId);

    /**
     * Retire un thème de la liste des thèmes suivis par un utilisateur.
     *
     * @param user    l’utilisateur concerné.
     * @param themeId l’identifiant du thème à ne plus suivre.
     * @return le {@link UserDTO} mis à jour.
     */
    UserDTO removeThemeToFollowed(UserDTO user, int themeId);
}
