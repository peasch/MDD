package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Comment;
import com.openclassrooms.mddapi.model.mappers.CommentMapper;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.CommentDAO;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implémentation du service {@link CommentService} pour la gestion des commentaires.
 * <p>
 * Cette classe contient la logique métier associée aux commentaires :
 * <ul>
 *     <li>Création et récupération des commentaires liés à un article ou un utilisateur,</li>
 *     <li>Vérification de l’existence d’un utilisateur ou d’un article avant ajout,</li>
 *     <li>Gestion des erreurs liées aux absences de commentaires ou d’entités.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Elle repose sur :
 * <ul>
 *     <li>{@link CommentDAO} pour les opérations de persistance,</li>
 *     <li>{@link CommentMapper} pour la conversion entité/DTO,</li>
 *     <li>{@link UserService} et {@link ArticleService} pour les validations croisées.</li>
 * </ul>
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    /** Mapper responsable de la conversion entre {@link Comment} et {@link CommentDTO}. */
    @Qualifier("commentMapper")
    private final CommentMapper mapper;

    /** Service de gestion des utilisateurs. */
    private final UserService userService;

    /** DAO gérant la persistance des commentaires. */
    private final CommentDAO commentDAO;

    /** Service de gestion des articles. */
    private final ArticleService articleService;

    /** Mapper pour les utilisateurs, utilisé pour certaines conversions indirectes. */
    @Qualifier("userMapper")
    private final UserMapper userMapper;

    /**
     * Récupère tous les commentaires présents dans la base de données.
     *
     * @return une liste de {@link CommentDTO}.
     */
    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentDAO.findAll();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
        return commentDTOS;
    }

    /**
     * Récupère tous les commentaires associés à un article.
     *
     * @param id l’identifiant de l’article.
     * @return une liste de {@link CommentDTO} liés à cet article.
     * @throws NoSuchElementException si l’article n’existe pas.
     */
    @Override
    public List<CommentDTO> getAllCommentsOfArticle(int id) {
        if (articleService.checkArticle(id)) {
            List<Comment> comments = commentDAO.findAllByArticleId(id);
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        } else {
            throw new NoSuchElementException("Article with id " + id + " not found");
        }
    }

    /**
     * Récupère tous les commentaires publiés par un utilisateur spécifique.
     *
     * @param id l’identifiant de l’utilisateur.
     * @return une liste de {@link CommentDTO} appartenant à cet utilisateur.
     * @throws NoSuchElementException si l’utilisateur n’existe pas ou n’a pas de commentaires.
     */
    @Override
    public List<CommentDTO> getAllCommentsOfUser(int id) {
        if (userService.checkId(id)) {
            List<Comment> comments = commentDAO.findAllByAuthorId(id);
            return checkNoComments(comments, id);
        } else {
            throw new NoSuchElementException("User with id : " + id + " not found");
        }
    }

    /**
     * Récupère tous les commentaires d’un utilisateur sur un article spécifique.
     *
     * @param userId    l’identifiant de l’utilisateur.
     * @param articleId l’identifiant de l’article.
     * @return une liste de {@link CommentDTO} correspondant aux critères.
     * @throws NoSuchElementException si l’utilisateur ou l’article n’existent pas.
     */
    @Override
    public List<CommentDTO> getAllCommentsOfUserAndArticle(int userId, int articleId) {
        if (userService.checkId(userId) && articleService.checkArticle(articleId)) {
            List<Comment> comments = commentDAO.findAllByAuthorIdAndArticleId(userId, articleId);
            return checkNoCommentsOrArticle(comments, userId, articleId);
        } else {
            throw new NoSuchElementException("User " + userId + " or article " + articleId + " not found");
        }
    }

    /**
     * Ajoute un nouveau commentaire à un article si l’utilisateur et l’article existent.
     *
     * @param articleId identifiant de l’article commenté.
     * @param userId    identifiant de l’auteur du commentaire.
     * @param content   contenu textuel du commentaire.
     * @return le commentaire ajouté sous forme de {@link CommentDTO}.
     * @throws NoSuchElementException si l’article ou l’utilisateur n’existent pas.
     */
    @Override
    public CommentDTO addCommentToArticle(int articleId, int userId, String content) {
        CommentDTO commentDTO = new CommentDTO();
        if (userService.checkId(userId) && articleService.checkArticle(articleId)) {
            commentDTO.setAuthorId(userId);
            commentDTO.setArticleId(articleId);
            commentDTO.setContent(content);
            commentDTO.setCreatedAt(new Date());
            return mapper.fromCommentToCommentDTO(commentDAO.save(mapper.fromDtoToComment(commentDTO)));
        } else {
            throw new NoSuchElementException("Article with id " + articleId + " not found");
        }
    }

    /**
     * Vérifie si un utilisateur possède des commentaires, sinon lève une exception.
     *
     * @param comments liste des commentaires à vérifier.
     * @param userId   identifiant de l’utilisateur concerné.
     * @return la liste convertie de {@link CommentDTO}.
     * @throws NoSuchElementException si l’utilisateur n’a aucun commentaire.
     */
    public List<CommentDTO> checkNoComments(List<Comment> comments, int userId) {
        if (comments.isEmpty()) {
            throw new NoSuchElementException("User with id : " + userId + " has no comments");
        } else {
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        }
    }

    /**
     * Vérifie si un utilisateur a commenté un article donné, sinon lève une exception.
     *
     * @param comments  liste des commentaires à vérifier.
     * @param userId    identifiant de l’utilisateur.
     * @param articleId identifiant de l’article.
     * @return la liste convertie de {@link CommentDTO}.
     * @throws NoSuchElementException si aucun commentaire n’est trouvé pour cet utilisateur et cet article.
     */
    public List<CommentDTO> checkNoCommentsOrArticle(List<Comment> comments, int userId, int articleId) {
        if (comments.isEmpty()) {
            throw new NoSuchElementException("User with id: " + userId + " has no comments on article: " + articleId);
        } else {
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        }
    }
}
