package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import com.openclassrooms.mddapi.model.mappers.ArticleMapper;
import com.openclassrooms.mddapi.repositories.ArticleDAO;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implémentation du service {@link ArticleService} pour la gestion des articles.
 * <p>
 * Cette classe contient la logique métier associée aux articles :
 * <ul>
 *     <li>Création, lecture, mise à jour et suppression d’articles</li>
 *     <li>Récupération des articles par thème</li>
 *     <li>Récupération des articles liés aux thèmes suivis par un utilisateur</li>
 * </ul>
 * </p>
 *
 * <p>
 * Elle s’appuie sur les composants suivants :
 * <ul>
 *     <li>{@link ArticleDAO} pour les opérations de persistance,</li>
 *     <li>{@link ArticleMapper} pour la conversion entre entités et DTOs,</li>
 *     <li>{@link UserService} pour les informations sur les auteurs,</li>
 *     <li>{@link ThemeService} pour les informations de thème.</li>
 * </ul>
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    /** DAO permettant d’effectuer les opérations CRUD sur les articles. */
    private final ArticleDAO articleDAO;

    /** Mapper responsable de la conversion entre entités et DTOs. */
    @Qualifier("articleMapper")
    private final ArticleMapper mapper;

    /** Service de gestion des utilisateurs. */
    private final UserService userService;

    /** Service de gestion des thèmes. */
    private final ThemeService themeService;

    /**
     * Récupère la liste de tous les articles disponibles.
     *
     * @return une liste de {@link ArticleDTO}.
     */
    @Override
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleDAO.findAll();
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        return articlesDTO;
    }

    /**
     * Récupère tous les articles appartenant à un thème spécifique.
     *
     * @param id l’identifiant du thème.
     * @return une liste d’articles associés au thème.
     */
    @Override
    public List<ArticleDTO> getAllArticlesOfTheme(int id) {
        List<Article> articles = articleDAO.findArticlesByThemeId(id);
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        articlesDTO.forEach(article ->
                article.setAuthorUsername(userService.getUserById(article.getAuthorId()).getUsername()));
        return articlesDTO;
    }

    /**
     * Enregistre un nouvel article dans la base de données.
     *
     * @param article les informations de l’article à sauvegarder.
     * @return l’article sauvegardé sous forme de {@link ArticleDTO}.
     */
    @Override
    public ArticleDTO save(ArticleDTO article) {
        UserDTO author = userService.getUserById(article.getAuthorId());
        ThemeDTO theme = themeService.getThemeById(article.getThemeId());
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setThemeId(theme.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setAuthorId(author.getId());
        articleDTO.setCreatedAt(new Date());

        return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleDTO)));
    }

    /**
     * Met à jour un article existant si l’utilisateur est bien son auteur.
     *
     * @param id          l’identifiant de l’article à mettre à jour.
     * @param articleDTO  les nouvelles informations de l’article.
     * @param userDTO     l’utilisateur demandant la mise à jour.
     * @return l’article mis à jour sous forme de {@link ArticleDTO}.
     * @throws NoSuchElementException si l’utilisateur n’est pas l’auteur de l’article.
     */
    @Override
    public ArticleDTO updateArticle(int id, ArticleDTO articleDTO, UserDTO userDTO) {
        ArticleDTO articleToUpdate = mapper.fromArticleToDto(articleDAO.findById(id));
        UserDTO articleUser = userService.getUserById(articleDTO.getAuthorId());
        if (userDTO.getEmail().equals(articleUser.getEmail())) {
            articleToUpdate.setContent(articleDTO.getContent());
            articleToUpdate.setAuthorId(articleDTO.getAuthorId());
            articleToUpdate.setThemeId(articleDTO.getThemeId());
            articleToUpdate.setUpdatedAt(new Date());
            return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleToUpdate)));
        } else {
            throw new NoSuchElementException("vous n'êtes pas l'auteur de l'article");
        }
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id l’identifiant de l’article recherché.
     * @return l’article correspondant sous forme de {@link ArticleDTO}.
     * @throws NoSuchElementException si aucun article n’est trouvé.
     */
    @Override
    public ArticleDTO getArticle(int id) {
        if (checkArticle(id)) {
            ArticleDTO article = mapper.fromArticleToDto(articleDAO.findById(id));
            article.setAuthorUsername(userService.getUserById(article.getAuthorId()).getUsername());
            return article;
        } else {
            throw new NoSuchElementException("Article with id " + id + " not found");
        }
    }

    /**
     * Supprime un article si l’utilisateur est bien son auteur.
     *
     * @param id       l’identifiant de l’article à supprimer.
     * @param userDTO  l’utilisateur demandant la suppression.
     * @throws NoSuchElementException si l’article n’existe pas ou si l’utilisateur n’en est pas l’auteur.
     */
    @Override
    public void deleteArticle(int id, UserDTO userDTO) {
        ArticleDTO articleToDelete = mapper.fromArticleToDto(articleDAO.findById(id));
        UserDTO articleUser = userService.getUserById(articleToDelete.getAuthorId());
        if (articleDAO.existsById(id) && userDTO.getEmail().equals(articleUser.getEmail())) {
            articleDAO.deleteById(id);
        } else {
            throw new NoSuchElementException("vous n'êtes pas l'auteur de l'article");
        }
    }

    /**
     * Vérifie si un article existe à partir de son identifiant.
     *
     * @param id l’identifiant de l’article.
     * @return {@code true} si l’article existe, sinon {@code false}.
     */
    @Override
    public boolean checkArticle(int id) {
        return articleDAO.existsById(id);
    }

    /**
     * Récupère tous les articles liés aux thèmes suivis par un utilisateur.
     *
     * @param user l’utilisateur dont on souhaite récupérer les articles suivis.
     * @return une liste de {@link ArticleDTO} appartenant aux thèmes suivis.
     */
    @Override
    public List<ArticleDTO> getAllFollowedArticles(UserDTO user) {
        List<ThemeDTO> followedThemes = user.getFollowedThemes();
        List<ArticleDTO> followedArticles = new ArrayList<>();

        for (ThemeDTO themeDTO : followedThemes) {
            List<ArticleDTO> articlesOfTheme = this.getAllArticlesOfTheme(themeDTO.getId());
            followedArticles.addAll(articlesOfTheme);
        }

        return followedArticles;
    }
}
