package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.ThemeService;
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

import java.util.HashMap;
import java.util.Map;

import static com.openclassrooms.mddapi.config.Constants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Contrôleur REST gérant les opérations liées aux thèmes.
 * <p>
 * Ce contrôleur expose plusieurs endpoints pour :
 * <ul>
 *   <li>Récupérer la liste de tous les thèmes disponibles</li>
 *   <li>Suivre un thème (follow)</li>
 *   <li>Ne plus suivre un thème (unfollow)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les endpoints nécessitant l’authentification utilisent un {@link Jwt}
 * afin d’identifier l’utilisateur connecté via la claim {@code sub}.
 * </p>
 *
 * <p>
 * Les réponses sont encapsulées dans une {@link Map} contenant des clés standardisées
 * comme {@code "themes"}, {@code "user"} ou {@code "message"}.
 * </p>
 *
 * @author PA-SCHAMING
 * @since 1.0
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theme/")
@Tag(name = "Theme Controller", description = "theme actions services")
public class ThemeController {

    /** Service métier de gestion des thèmes. */
    private final ThemeService themeService;

    /** Service métier de gestion des utilisateurs. */
    private final UserService userService;

    /**
     * Récupère la liste de tous les thèmes disponibles dans la base de données.
     *
     * @return {@link ResponseEntity} contenant la clé {@code "themes"} et la liste correspondante.
     *         Retourne le code HTTP 200 en cas de succès.
     */
    @Operation(summary = "get all themes", description = "Retrieve all available themes from the database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getThemes() {
        Map<Object, Object> model = new HashMap<>();
        model.put("themes", themeService.getAllThemes());
        return ok(model);
    }

    /**
     * Permet à l’utilisateur authentifié de suivre un thème.
     *
     * @param themeId   identifiant du thème à suivre
     * @param principal jeton JWT de l’utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code "user"} avec l’utilisateur mis à jour.
     *         Retourne le code 403 si une erreur se produit.
     */
    @Operation(summary = "follow theme", description = "Add a theme to the list of themes followed by the logged-in user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "403", description = "forbidden or unexpected error")
    @GetMapping("/follow/{id}")
    public ResponseEntity<Map<Object, Object>> follow(@PathVariable(name = "id") int themeId,
                                                      @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();

        try {
            UserDTO userUpdated = userService.addThemeToFollowed(userLoggedIn, themeId);
            model.put("user", userUpdated);
            log.info("Followed theme ! " + userUpdated);
            return ok(model);
        } catch (Exception e) {
            model.put(MESSAGE, SOMETHING_WRONG_FOLLOW);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Permet à l’utilisateur authentifié d’arrêter de suivre un thème.
     *
     * @param themeId   identifiant du thème à ne plus suivre
     * @param principal jeton JWT de l’utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code "user"} avec l’utilisateur mis à jour.
     *         Retourne le code 403 si une erreur survient.
     */
    @Operation(summary = "unfollow theme", description = "Remove a theme from the list of themes followed by the logged-in user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "403", description = "forbidden or unexpected error")
    @GetMapping("/unfollow/{id}")
    public ResponseEntity<Map<Object, Object>> unFollow(@PathVariable(name = "id") int themeId,
                                                        @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();

        try {
            UserDTO userUpdated = userService.removeThemeToFollowed(userLoggedIn, themeId);
            model.put("user", userUpdated);
            log.info("Unfollowed theme ! " + userUpdated);
            return ok(model);
        } catch (Exception e) {
            model.put(MESSAGE, SOMETHING_WRONG_FOLLOW);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }
}
