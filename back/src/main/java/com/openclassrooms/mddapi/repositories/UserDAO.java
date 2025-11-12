package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de gestion des opérations de persistance pour les entités {@link User}.
 * <p>
 * Cette interface étend {@link JpaRepository} afin de bénéficier de toutes les fonctionnalités
 * standards de Spring Data JPA (CRUD, pagination, tri, etc.).
 * </p>
 *
 * <p>
 * Elle définit également plusieurs méthodes de recherche spécifiques aux utilisateurs,
 * basées sur les attributs {@code email}, {@code username} et {@code id}.
 * </p>
 *
 * <p>
 * Les implémentations de ces méthodes sont automatiquement générées par Spring Data
 * à partir de leurs noms, sans qu’il soit nécessaire d’écrire du code SQL ou JPQL.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param email l’adresse e-mail de l’utilisateur.
     * @return l’utilisateur correspondant, ou {@code null} s’il n’existe pas.
     */
    User findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d’utilisateur (username).
     *
     * @param username le nom d’utilisateur recherché.
     * @return l’utilisateur correspondant, ou {@code null} si aucun n’est trouvé.
     */
    User findByUsername(String username);

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l’identifiant de l’utilisateur.
     * @return l’utilisateur correspondant, ou {@code null} s’il n’existe pas.
     */
    User findById(int id);

    /**
     * Récupère la liste de tous les utilisateurs enregistrés.
     *
     * @return une liste de toutes les entités {@link User}.
     */
    List<User> findAll();

    /**
     * Supprime un utilisateur en fonction de son identifiant.
     *
     * @param id l’identifiant de l’utilisateur à supprimer.
     */
    void deleteById(Integer id);
}
