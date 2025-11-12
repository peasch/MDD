package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service personnalisé pour le chargement des détails d'un utilisateur.
 * <p>
 * Cette classe implémente {@link UserDetailsService} et est utilisée par Spring Security
 * pour récupérer les informations d'authentification d'un utilisateur à partir de la base de données.
 * </p>
 *
 * <p>
 * Elle s'appuie sur {@link UserService} pour interroger le stockage des utilisateurs
 * et renvoie un objet {@link UserDetails} reconnu par Spring Security.
 * </p>
 *
 * <p>
 * Annotée avec {@link Service}, elle est automatiquement détectée et injectée par Spring.
 * </p>
 *
 * @author PA SCHAMING
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Service permettant de récupérer les informations des utilisateurs.
     */
    private final UserService userService;

    /**
     * Charge les détails d’un utilisateur à partir de son adresse e-mail (utilisée comme nom d’utilisateur).
     *
     * @param username le nom d’utilisateur (ici, l’adresse e-mail)
     * @return un objet {@link UserDetails} contenant les informations d’authentification
     * @throws UsernameNotFoundException si aucun utilisateur correspondant n’est trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userdto = userService.getUserByEmail(username);

        if (userdto != null) {
            // Création d’un utilisateur Spring Security sans rôles spécifiques (liste vide)
            return new User(userdto.getEmail(), userdto.getPassword(), new ArrayList<>());
        }

        // Si aucun utilisateur n’est trouvé, on lève une exception standard de Spring Security
        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'adresse e-mail : " + username);
    }

}
