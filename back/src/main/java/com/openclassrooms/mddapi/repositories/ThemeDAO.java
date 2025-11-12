package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de gestion des opérations de persistance pour les entités {@link Theme}.
 * <p>
 * Cette interface étend {@link JpaRepository} pour fournir automatiquement
 * l’ensemble des opérations CRUD standard (création, lecture, mise à jour, suppression)
 * sur la table {@code theme}.
 * </p>
 *
 * <p>
 * Grâce à Spring Data JPA, aucune implémentation manuelle n’est nécessaire :
 * les méthodes par défaut de {@link JpaRepository} couvrent la majorité des besoins.
 * </p>
 *
 * <p>
 * Si des recherches personnalisées sont nécessaires (par exemple : trouver un thème
 * par son nom ou sa description), elles peuvent être ajoutées ici en suivant la
 * convention de nommage de Spring Data (ex : {@code findByName(String name)}).
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface ThemeDAO extends JpaRepository<Theme, Integer> {
    // Méthodes spécifiques de requête à ajouter ici si besoin.
}
