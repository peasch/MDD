package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.UserDTO;
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

import static com.openclassrooms.mddapi.config.Constants.MESSAGE;
import static com.openclassrooms.mddapi.config.Constants.THEMES;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Contrôleur REST gérant les opérations liées aux {@link UserDTO}.
 * <p>
 * Endpoints principaux :
 * <ul>
 *     <li>Suppression d'un utilisateur par identifiant</li>
 *     <li>Récupération des thèmes suivis par l'utilisateur connecté</li>
 *     <li>Récupération d'un utilisateur par identifiant</li>
 *     <li>Mise à jour du profil de l'utilisateur (restreint à l'utilisateur lui-même)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les endpoints nécessitant l'identité courante utilisent un {@link Jwt} (claim {@code sub})
 * pour retrouver l'utilisateur connecté.
 * </p>
 *
 * <p>
 * Les réponses sont encapsulées dans un {@link Map} avec des clés stables comme
 * {@code "user"}, {@code "themes"} et {@code "message"}.
 * </p>
 *
 * @author PA-SCHAMING
 * @since 1.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
@Tag(name = "User Controller", description = "all users services")
public class UserController {

    /** Service métier de gestion des utilisateurs. */
    private final UserService userService;

    /**
     * Supprime un utilisateur par identifiant.
     *
     * <p>Retourne :</p>
     * <ul>
     *   <li><b>200</b> si la suppression réussit</li>
     *   <li><b>404</b> si l'utilisateur n'existe pas</li>
     *   <li><b>400</b> si l'identifiant n'est pas un entier valide</li>
     * </ul>
     *
     * @param id identifiant de l'utilisateur à supprimer (format chaîne, parsé en entier)
     * @return une {@link ResponseEntity} vide avec le code HTTP approprié
     */
    @Operation(summary = "delete user", description = "delete then chosen user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            UserDTO user = this.userService.getUserById(Integer.parseInt(id));

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            this.userService.deleteUserById(Integer.parseInt(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère la liste des thèmes suivis par l'utilisateur actuellement connecté.
     *
     * @param principal jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code THEMES} et un {@code MESSAGE}
     */
    @Operation(summary = "get followed themes of current user", description = "retrieve all themes followed by the logged-in user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/followed")
    public ResponseEntity<Map<Object, Object>> getfollowedThemeOfUser(@AuthenticationPrincipal Jwt principal) {

        Map<Object, Object> model = new HashMap<>();
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        model.put(THEMES, userLoggedIn.getFollowedThemes());
        model.put(MESSAGE, "All themes of user:" + userLoggedIn.getEmail());
        return ok(model);
    }

    /**
     * Récupère un utilisateur par identifiant.
     *
     * @param id        identifiant de l'utilisateur à récupérer (format chaîne, parsé en entier)
     * @param principal jeton JWT de l'utilisateur authentifié (non utilisé ici mais disponible pour contrôle/traçage)
     * @return {@link ResponseEntity} contenant la clé {@code "user"} avec l'utilisateur trouvé (ou {@code null} s'il n'existe pas)
     */
    @Operation(summary = "get user by id", description = "retrieve a user by its id")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getUser(@PathVariable("id") String id,
                                                       @AuthenticationPrincipal Jwt principal) {

        Map<Object, Object> model = new HashMap<>();
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        model.put("user", userService.getUserById(Integer.parseInt(id)));

        return ok(model);
    }

    /**
     * Met à jour le profil d'un utilisateur. Seul l'utilisateur connecté peut modifier son propre profil.
     *
     * <p>Retourne :</p>
     * <ul>
     *   <li><b>200</b> avec la clé {@code "user"} si la mise à jour réussit</li>
     *   <li><b>403</b> si l'utilisateur connecté tente de modifier un autre utilisateur</li>
     *   <li><b>403</b> si une erreur survient durant la mise à jour</li>
     * </ul>
     *
     * @param userDto   données à jour de l'utilisateur
     * @param principal jeton JWT de l'utilisateur authentifié
     * @return {@link ResponseEntity} contenant l'utilisateur mis à jour ou une erreur
     */
    @Operation(summary = "update user", description = "update user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PutMapping("/update")
    public ResponseEntity<Map<Object, Object>> updateUser(@RequestBody UserDTO userDto,
                                                          @AuthenticationPrincipal Jwt principal) {
        Map<Object, Object> model = new HashMap<>();
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        try {
            if (userDto.getId() == userLoggedIn.getId()) {
                model.put("user", userService.updateUser(userDto));
                return ok(model);
            } else {
                Map<Object, Object> error = new HashMap<>();
                error.put(MESSAGE, "you're not able to modify this user");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
        } catch (Exception e) {
            Map<Object, Object> error = new HashMap<>();
            error.put("message", "error with registration");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }
}
