package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.ok;
import static com.openclassrooms.mddapi.config.Constants.*;

/**
 * Contrôleur REST gérant les actions relatives aux {@link CommentDTO}.
 * <p>
 * Fournit plusieurs endpoints permettant de :
 * <ul>
 *   <li>Récupérer tous les commentaires</li>
 *   <li>Récupérer les commentaires d’un article</li>
 *   <li>Récupérer les commentaires d’un utilisateur</li>
 *   <li>Récupérer les commentaires d’un utilisateur sur un article donné</li>
 *   <li>Ajouter un nouveau commentaire à un article</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque réponse est encapsulée dans un {@code Map<Object, Object>} contenant les clés comme
 * {@code "comments"}, {@code "article"}, {@code "user"}, et {@code "message"}.
 * </p>
 *
 * <p>
 * L’accès à certaines méthodes requiert un utilisateur authentifié via un {@link Jwt}.
 * </p>
 *
 * @author PA-SCHAMING
 * @since 1.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/")
@Tag(name = "Comments Controller", description = "comments actions services")
public class CommentsController {

    /** Service métier gérant les utilisateurs. */
    private final UserService userService;

    /** Service métier gérant les commentaires. */
    private final CommentService commentService;

    /**
     * Récupère la liste de tous les commentaires présents en base.
     *
     * @return {@link ResponseEntity} contenant une map avec la clé {@code "comments"} et un message de succès.
     */
    @Operation(summary = "get all comments method", description = "get all comments in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getComments() {
        Map<Object, Object> model = new HashMap<>();
        model.put(COMMENTS, commentService.getAllComments());
        model.put(MESSAGE, "All comments !");
        return ok(model);
    }

    /**
     * Récupère tous les commentaires associés à un article donné.
     *
     * @param id identifiant de l’article
     * @return {@link ResponseEntity} contenant la clé {@code "comments"} si l’article existe.
     *         Retourne un code 404 si l’article n’existe pas.
     */
    @Operation(summary = "get comments of an article", description = "get all comments of a specific article")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "404", description = "article not found")
    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getCommentsOfArticleId(@PathVariable(name = "id") int id) {
        Map<Object, Object> model = new HashMap<>();
        try {
            model.put(COMMENTS, commentService.getAllCommentsOfArticle(id));
            return ok(model);
        } catch (NoSuchElementException _) {
            model.put(MESSAGE, "article not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    /**
     * Récupère tous les commentaires rédigés par un utilisateur spécifique.
     *
     * @param id identifiant de l’utilisateur
     * @return {@link ResponseEntity} contenant la clé {@code "comments"} si l’utilisateur existe.
     *         Retourne 404 si aucun utilisateur correspondant n’est trouvé.
     */
    @Operation(summary = "get comments of a user", description = "get all comments of a specific user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "404", description = "user not found")
    @GetMapping("/author/{id}")
    public ResponseEntity<Map<Object, Object>> getCommentsOfUserId(@PathVariable(name = "id") int id) {
        Map<Object, Object> model = new HashMap<>();
        model.put("user", id);
        try {
            model.put(COMMENTS, commentService.getAllCommentsOfUser(id));
            log.info("get comments of user id :" + id);
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    /**
     * Récupère les commentaires postés par l’utilisateur connecté sur un article précis.
     *
     * @param principal jeton JWT de l’utilisateur authentifié
     * @param articleId identifiant de l’article concerné (passé en paramètre de requête)
     * @return {@link ResponseEntity} contenant la clé {@code "comments"} si des commentaires existent.
     *         Retourne 404 si aucun commentaire trouvé ou article inexistant.
     */
    @Operation(summary = "get comments of user and article", description = "get all comments from the logged-in user for a given article")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "404", description = "not found")
    @GetMapping("/user/article/")
    public ResponseEntity<Map<Object, Object>> getCommentsOfUserIdAndArticleId(@AuthenticationPrincipal Jwt principal,
                                                                               @Valid @RequestParam("article") int articleId) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        int userId = userLoggedIn.getId();
        Map<Object, Object> model = new HashMap<>();
        model.put("user", userId);
        model.put("article", articleId);

        try {
            model.put(COMMENTS, commentService.getAllCommentsOfUserAndArticle(userId, articleId));
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    /**
     * Ajoute un nouveau commentaire à un article.
     * L’utilisateur doit être authentifié pour publier.
     *
     * @param principal jeton JWT de l’utilisateur authentifié
     * @param articleId identifiant de l’article concerné
     * @param content   contenu textuel du commentaire
     * @return {@link ResponseEntity} contenant la clé {@code "comment saved"} avec le {@link CommentDTO} créé.
     *         Retourne 404 si l’article n’existe pas.
     */
    @Operation(summary = "add comment to article", description = "add a new comment to a specific article")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "404", description = "article not found")
    @PostMapping("/add/{id}")
    public ResponseEntity<Map<Object, Object>> addCommentToArticle(@AuthenticationPrincipal Jwt principal,
                                                                   @PathVariable(name = "id") int articleId,
                                                                   @Valid @RequestBody String content) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        int userId = userLoggedIn.getId();
        Map<Object, Object> model = new HashMap<>();
        model.put("user", userId);
        model.put("article", articleId);

        try {
            CommentDTO commentDTO = commentService.addCommentToArticle(articleId, userId, content);
            model.put("comment saved", commentDTO);
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }
}
