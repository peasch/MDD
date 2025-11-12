package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de gestion des opérations de persistance pour les entités {@link Comment}.
 * <p>
 * Cette interface étend {@link JpaRepository} afin de bénéficier des opérations
 * CRUD standard fournies par Spring Data JPA, tout en ajoutant des méthodes
 * personnalisées de recherche de commentaires selon différents critères.
 * </p>
 *
 * <p>
 * Les implémentations de ces méthodes sont automatiquement générées par Spring Data
 * à partir de leur nom, sans qu’il soit nécessaire d’écrire du code SQL ou JPQL.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface CommentDAO extends JpaRepository<Comment, Long> {

    /**
     * Récupère la liste de tous les commentaires publiés par un auteur spécifique.
     *
     * @param authorId l’identifiant de l’auteur.
     * @return la liste des commentaires associés à cet auteur.
     */
    List<Comment> findAllByAuthorId(int authorId);

    /**
     * Récupère la liste de tous les commentaires associés à un article spécifique.
     *
     * @param id l’identifiant de l’article.
     * @return la liste des commentaires liés à cet article.
     */
    List<Comment> findAllByArticleId(int id);

    /**
     * Récupère la liste des commentaires associés à un article et publiés par un auteur spécifique.
     *
     * @param authorId l’identifiant de l’auteur.
     * @param articleId l’identifiant de l’article.
     * @return la liste des commentaires correspondant à l’auteur et à l’article donnés.
     */
    List<Comment> findAllByAuthorIdAndArticleId(int authorId, int articleId);

    /**
     * Sauvegarde un commentaire dans la base de données.
     * <p>
     * Si le commentaire n’existe pas encore, il est inséré ; sinon, il est mis à jour.
     * </p>
     *
     * @param comment le commentaire à sauvegarder.
     * @return le commentaire persistant sauvegardé.
     */
    Comment save(Comment comment);
}
