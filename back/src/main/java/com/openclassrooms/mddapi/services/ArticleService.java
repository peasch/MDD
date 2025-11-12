package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;

import java.util.List;

/**
 * Service définissant les opérations métier liées à la gestion des articles.
 * <p>
 * Cette interface définit les fonctionnalités principales du module article :
 * <ul>
 *     <li>Récupération de tous les articles ou par thème,</li>
 *     <li>Création et mise à jour d’un article,</li>
 *     <li>Suppression d’un article,</li>
 *     <li>Récupération d’un article spécifique,</li>
 *     <li>Vérification de l’existence d’un article,</li>
 *     <li>Récupération des articles liés aux thèmes suivis par un utilisateur.</li>
 * </ul>
 * </p>
 *
 * <p>
 * L’implémentation par défaut de cette interface est {@link com.openclassrooms.mddapi.services.impl.ArticleServiceImpl}.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface ArticleService {

    /**
     * Récupère la liste complète des articles disponibles.
     *
     * @return une liste de {@link ArticleDTO}.
     */
    List<ArticleDTO> getArticles();

    /**
     * Récupère tous les articles appartenant à un thème donné.
     *
     * @param id l’identifiant du thème.
     * @return une liste d’articles liés à ce thème.
     */
    List<ArticleDTO> getAllArticlesOfTheme(int id);

    /**
     * Enregistre un nouvel article dans la base de données.
     *
     * @param article les informations de l’article à enregistrer.
     * @return l’article sauvegardé sous forme de {@link ArticleDTO}.
     */
    ArticleDTO save(ArticleDTO article);

    /**
     * Met à jour un article existant, si l’utilisateur est bien son auteur.
     *
     * @param id         l’identifiant de l’article à mettre à jour.
     * @param articleDTO les nouvelles données de l’article.
     * @param userDTO    l’utilisateur demandant la modification.
     * @return l’article mis à jour.
     */
    ArticleDTO updateArticle(int id, ArticleDTO articleDTO, UserDTO userDTO);

    /**
     * Récupère un article à partir de son identifiant.
     *
     * @param id l’identifiant de l’article.
     * @return l’article correspondant sous forme de {@link ArticleDTO}.
     */
    ArticleDTO getArticle(int id);

    /**
     * Supprime un article, si l’utilisateur est bien son auteur.
     *
     * @param id      l’identifiant de l’article à supprimer.
     * @param userDTO l’utilisateur demandant la suppression.
     */
    void deleteArticle(int id, UserDTO userDTO);

    /**
     * Vérifie l’existence d’un article par son identifiant.
     *
     * @param id l’identifiant de l’article.
     * @return {@code true} si l’article existe, sinon {@code false}.
     */
    boolean checkArticle(int id);

    /**
     * Récupère les articles liés aux thèmes suivis par un utilisateur.
     *
     * @param user l’utilisateur dont on veut les articles suivis.
     * @return une liste d’articles correspondant aux thèmes suivis.
     */
    List<ArticleDTO> getAllFollowedArticles(UserDTO user);
}
