package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de gestion des opérations de persistance pour les entités {@link Article}.
 * <p>
 * Cette interface étend {@link JpaRepository} afin de bénéficier des fonctionnalités
 * standards de Spring Data JPA (CRUD complet, pagination, tri, etc.).
 * </p>
 *
 * <p>
 * Elle déclare également des méthodes spécifiques pour la recherche d’articles
 * selon certains critères, comme le thème associé.
 * </p>
 *
 * <p>
 * Les implémentations sont générées automatiquement par Spring Data JPA,
 * aucune implémentation manuelle n’est nécessaire.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface ArticleDAO extends JpaRepository<Article, Integer> {

    /**
     * Recherche un article par son identifiant.
     *
     * @param id l’identifiant de l’article.
     * @return l’entité {@link Article} correspondante, ou {@code null} si non trouvée.
     */
    Article findById(int id);

    /**
     * Récupère la liste complète de tous les articles.
     *
     * @return une liste de toutes les entités {@link Article}.
     */
    List<Article> findAll();

    /**
     * Supprime un article en fonction de son identifiant.
     *
     * @param id l’identifiant de l’article à supprimer.
     */
    void deleteById(Integer id);

    /**
     * Récupère la liste des articles associés à un thème donné.
     *
     * @param themeId l’identifiant du thème.
     * @return une liste d’articles appartenant au thème spécifié.
     */
    List<Article> findArticlesByThemeId(int themeId);
}
