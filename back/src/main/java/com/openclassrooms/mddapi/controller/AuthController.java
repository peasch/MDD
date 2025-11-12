package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.LoginDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static com.openclassrooms.mddapi.config.Constants.*;

/**
 * Contrôleur d’authentification gérant la connexion, l’inscription
 * et la récupération des informations de l’utilisateur connecté.
 * <p>
 * Expose les endpoints REST relatifs à l'authentification :
 * <ul>
 *   <li><b>/login</b> : connexion d’un utilisateur existant</li>
 *   <li><b>/register</b> : inscription d’un nouvel utilisateur</li>
 *   <li><b>/me</b> : récupération des informations de l’utilisateur courant</li>
 * </ul>
 * </p>
 *
 * <p>
 * Utilise un {@link JWTService} pour la génération des tokens JWT
 * et un {@link UserService} pour les opérations liées aux utilisateurs.
 * </p>
 *
 * @author PA-SCHAMING
 * @since 1.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
@Tag(name = "Authentication Controller", description = "register and login service")
public class AuthController {

    /** Service de génération et validation des tokens JWT. */
    public final JWTService jwtService;

    /** Service métier pour la gestion des utilisateurs. */
    private final UserService userService;

    /** Gestionnaire d’authentification Spring Security. */
    private final AuthenticationManager authenticationManager;

    /**
     * Authentifie un utilisateur à partir de ses identifiants (email ou nom d’utilisateur + mot de passe),
     * puis renvoie un token JWT s’il est valide.
     *
     * @param logintDto objet contenant les identifiants de connexion (email/username + mot de passe)
     * @return {@link ResponseEntity} contenant la clé {@code "token"} si la connexion réussit.
     *         Retourne le code 403 en cas d’échec d’authentification ou d’erreur.
     */
    @Operation(summary = "login method", description = "clic here to login in with your credentials, email & password")
    @ApiResponse(responseCode = "200", description = "You're logged in")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody LoginDTO logintDto) {
        Map<Object, Object> model = new HashMap<>();

        try {
            String identifier = logintDto.getEmail();

            UserDTO user;
            if (identifier.contains("@")) {
                // Connexion par email
                user = userService.getUserByEmail(identifier);
            } else {
                // Connexion par nom d’utilisateur
                user = userService.getUserByUsername(identifier);
            }

            if (user == null) {
                model.put("message", "Bad Credentials before authenticate");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
            }

            // Authentifie l’utilisateur auprès de Spring Security
            logintDto.setEmail(user.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logintDto.getEmail(), logintDto.getPassword())
            );

            UserDTO userLoggedin = userService.getUserByEmail(logintDto.getEmail());
            String token = jwtService.generateToken(userLoggedin);

            model.put("token", token);
            return ok(model);

        } catch (Exception e) {
            log.error(e.getMessage());
            model.put(ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    /**
     * Enregistre un nouvel utilisateur à partir des données fournies.
     * Si l’adresse email existe déjà, la requête est rejetée.
     * Un token JWT est automatiquement généré pour l’utilisateur inscrit.
     *
     * @param userDto données du nouvel utilisateur à enregistrer
     * @return {@link ResponseEntity} contenant la clé {@code "token"} pour l’utilisateur enregistré.
     *         Retourne 403 si l’email est déjà utilisé ou si une erreur se produit.
     */
    @Operation(summary = "register method", description = "clic here to register, with your details")
    @ApiResponse(responseCode = "200", description = "Welcome in, you're registered")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/register")
    public ResponseEntity<Map<Object, Object>> register(@RequestBody UserDTO userDto) {

        if (userService.getUserByEmail(userDto.getEmail()) == null) {
            try {
                userService.saveUser(userDto);
                UserDTO saved = userService.getUserByEmail(userDto.getEmail());
                Map<Object, Object> model = new HashMap<>();

                model.put("token", jwtService.generateToken(saved));
                return ok(model);
            } catch (Exception e) {
                Map<Object, Object> error = new HashMap<>();
                error.put("message", "error with registration");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
        } else {
            Map<Object, Object> error = new HashMap<>();
            error.put(MESSAGE, "Email already registered");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    /**
     * Récupère les informations de l’utilisateur actuellement connecté,
     * identifiées via le token JWT (claim {@code sub}).
     *
     * @param principal jeton JWT de l’utilisateur authentifié
     * @return {@link ResponseEntity} contenant la clé {@code "user"} avec les données de l’utilisateur connecté.
     */
    @ApiResponse(responseCode = "200", description = "here are your informations")
    @ApiResponse(responseCode = "400", description = "error")
    @GetMapping("/me")
    public ResponseEntity<Map<Object, Object>> getMe(@AuthenticationPrincipal Jwt principal) {
        Map<Object, Object> model = new HashMap<>();
        model.put("user", userService.getUserByEmail(principal.getClaimAsString("sub")));
        return ok(model);
    }


}
