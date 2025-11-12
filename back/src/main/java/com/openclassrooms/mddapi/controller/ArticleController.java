package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.ArticleService;
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
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static com.openclassrooms.mddapi.config.Constants.*;

/**
 * Contrôleur REST exposant les endpoints liés aux {@link ArticleDTO}.
 * <p>
 * Les réponses retournent systématiquement un {@code Map<Object, Object>} pour
 * permettre d'embarquer la/les ressources sous des clés stables
 * (ex. {@code "article"} ou {@code "articles"}), ainsi que d'éventuels messages.
 * </p>
 *
 * <p>
 * La sécurité repose sur un {@link Jwt} fourni par Spring Security :
 * l'email de l'utilisateur connecté est extrait via la claim {@code sub}.
 * </p>
 *
 * @author VotreNom
 * @since 1.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/")
@Tag(name = "Articles Controller", description = "articles actions services")
public class ArticleController {

    /** Service métier pour la gestion des articles. */
    private final ArticleService service;

    /** Service métier pour la gestion des utilisateurs. */
    private final UserService userService;

    /**
     * Récupère l'ensemble des articles.
     *
     * @return {@link ResponseEntity} contenant une map avec la clé {@code "articles"}
     *         et la liste {@code List<ArticleDTO>} correspondante. Code 200 en cas de succès.
     */
    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getArticles() {
        Map<Object, Object> model = new HashMap<>();
        model.put("articles", service.getArticles());
        return ok(model);
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param id         identifiant technique de l'article
     * @param principal  jeton JWT de l'utilisateur authentifié (utilisé ici pour journalisation/contrôles ultérieurs)
     * @return {@link ResponseEntity} contenant une map avec la clé {@code "article"}.
     *         Renvoie 200 en cas de succès, 403 si une erreur métier survient.
     */
    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getArticleById(@PathVariable(name = "id") int id,
                                                              @AuthenticationPrincipal Jwt principal) {
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            model.put("article", service.getArticle(id));
            return ok(model);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Crée (soumet) un nouvel article.
     *
     * @param newArticleDTO données de l'article à créer (validées)
     * @param principal     jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code "article"} avec l'article créé.
     *         Renvoie 200 en cas de succès, 403 en cas d'erreur métier.
     */
    @Operation(summary = "submit an article method", description = "method to save an article")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/add")
    public ResponseEntity<Map<Object, Object>> createArticle(@Valid @RequestBody ArticleDTO newArticleDTO,
                                                             @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            ArticleDTO articleDTO = service.save(newArticleDTO);
            model.put("article", articleDTO);
            return ok(model);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Met à jour un article existant. L'opération n'est autorisée que pour l'auteur.
     *
     * @param id          identifiant de l'article à mettre à jour
     * @param articleDTO  nouvelles données (validées)
     * @param principal   jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code "article"} avec l'article mis à jour.
     *         Renvoie 200 en cas de succès, 403 si l'utilisateur n'est pas autorisé ou en cas d'erreur.
     */
    @Operation(summary = "update Article method", description = "update an article in database, only if author")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<Object, Object>> updateArticle(@PathVariable(name = "id") int id,
                                                             @Valid @RequestBody ArticleDTO articleDTO,
                                                             @AuthenticationPrincipal Jwt principal) {
        Map<Object, Object> model = new HashMap<>();
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        try {
            ArticleDTO articleUpdatedDTO = service.updateArticle(id, articleDTO, userLoggedIn);
            model.put("article", articleUpdatedDTO);
            return ok(model);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Récupère tous les articles associés à un thème donné.
     *
     * @param themeId identifiant du thème
     * @return {@link ResponseEntity} contenant une map avec la clé {@code "articles"}.
     *         Renvoie 200 en cas de succès, 403 en cas d'erreur métier.
     */
    @Operation(summary = "all articles of a theme", description = "get all articles in database, refer to a theme")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/theme/{id}")
    public ResponseEntity<Map<Object, Object>> getAllArticlesOfTheme(@PathVariable(name = "id") int themeId) {
        Map<Object, Object> model = new HashMap<>();

        try {
            List<ArticleDTO> articleDTOs = service.getAllArticlesOfTheme(themeId);
            model.put("articles", articleDTOs);
            return ok(model);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Récupère les articles liés aux thèmes suivis par l'utilisateur connecté.
     *
     * @param principal jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} contenant une map avec la clé {@code "articles"}.
     *         Renvoie 200 en cas de succès, 403 en cas d'erreur.
     */
    @Operation(summary = "all articles followed by user", description = "get all articles in database,followed bu the connected user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/followed")
    public ResponseEntity<Map<Object, Object>> getAllFollowedArticles(@AuthenticationPrincipal Jwt principal) {
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            List<ArticleDTO> articleDTOs = service.getAllFollowedArticles(userLoggedIn);
            model.put("articles", articleDTOs);
            return ok(model);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }

    }

    /**
     * Supprime un article. L'opération n'est autorisée que pour l'auteur.
     *
     * @param id        identifiant de l'article à supprimer
     * @param principal jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} vide avec code 200 si la suppression réussit,
     *         403 en cas d'erreur ou d'autorisation refusée.
     */
    @Operation(summary = "delete method", description = "delete an article from database, only if author")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @DeleteMapping({"delete/{id}"})
    public ResponseEntity<Map<Object, Object>> deleteArticle(@PathVariable(name = "id") int id,
                                                             @AuthenticationPrincipal Jwt principal) {
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            service.deleteArticle(id, userLoggedIn);
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }
}
