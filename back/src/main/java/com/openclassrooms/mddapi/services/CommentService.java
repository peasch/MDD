package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.CommentDTO;

import java.util.List;

/**
 * Service définissant les opérations métier liées à la gestion des commentaires.
 * <p>
 * Cette interface regroupe les principales fonctionnalités permettant de :
 * <ul>
 *     <li>Récupérer tous les commentaires,</li>
 *     <li>Récupérer les commentaires associés à un article ou à un utilisateur,</li>
 *     <li>Ajouter un commentaire à un article existant.</li>
 * </ul>
 * </p>
 *
 * <p>
 * L’implémentation par défaut de cette interface est {@link com.openclassrooms.mddapi.services.impl.CommentServiceImpl}.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface CommentService {

    /**
     * Récupère la liste complète de tous les commentaires.
     *
     * @return une liste de {@link CommentDTO}.
     */
    List<CommentDTO> getAllComments();

    /**
     * Récupère tous les commentaires liés à un article spécifique.
     *
     * @param id l’identifiant de l’article.
     * @return une liste de {@link CommentDTO} associés à cet article.
     */
    List<CommentDTO> getAllCommentsOfArticle(int id);

    /**
     * Récupère tous les commentaires publiés par un utilisateur donné.
     *
     * @param id l’identifiant de l’utilisateur.
     * @return une liste de {@link CommentDTO} appartenant à cet utilisateur.
     */
    List<CommentDTO> getAllCommentsOfUser(int id);

    /**
     * Récupère tous les commentaires publiés par un utilisateur sur un article spécifique.
     *
     * @param userId    l’identifiant de l’utilisateur.
     * @param articleId l’identifiant de l’article.
     * @return une liste de {@link CommentDTO} correspondant aux critères donnés.
     */
    List<CommentDTO> getAllCommentsOfUserAndArticle(int userId, int articleId);

    /**
     * Ajoute un nouveau commentaire à un article.
     *
     * @param articleId identifiant de l’article concerné.
     * @param userId    identifiant de l’auteur du commentaire.
     * @param content   contenu du commentaire.
     * @return le commentaire ajouté sous forme de {@link CommentDTO}.
     */
    CommentDTO addCommentToArticle(int articleId, int userId, String content);
}
