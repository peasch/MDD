package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.User;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.UserDAO;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

/**
 * Implémentation du service {@link UserService} pour la gestion des utilisateurs.
 * <p>
 * Cette classe contient la logique métier liée aux utilisateurs, notamment :
 * <ul>
 *     <li>La gestion des comptes utilisateurs (création, mise à jour, suppression),</li>
 *     <li>La récupération d’utilisateurs par identifiant, e-mail ou nom d’utilisateur,</li>
 *     <li>La gestion des thèmes suivis par un utilisateur.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Elle repose sur :
 * <ul>
 *     <li>{@link UserDAO} pour la persistance des utilisateurs,</li>
 *     <li>{@link UserMapper} pour la conversion entre entités et DTOs,</li>
 *     <li>{@link BCryptPasswordEncoder} pour le chiffrement des mots de passe,</li>
 *     <li>{@link ThemeService} pour la gestion des thèmes suivis.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Annotée avec {@link Service}, elle est automatiquement gérée par Spring.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    /** Encodeur de mot de passe utilisé pour le chiffrement avant persistance. */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /** DAO gérant les opérations de persistance sur les utilisateurs. */
    private final UserDAO userDao;

    /** Service permettant de gérer les thèmes suivis par les utilisateurs. */
    private final ThemeService themeService;

    /** Mapper responsable de la conversion entre entités {@link User} et DTOs {@link UserDTO}. */
    @Qualifier("userMapper")
    private final UserMapper mapper;

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l’identifiant de l’utilisateur.
     * @return le {@link UserDTO} correspondant.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public UserDTO getUserById(int id) {
        return mapper.fromUserToDtoWithoutPassword(userDao.findById(id));
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id l’identifiant de l’utilisateur à supprimer.
     */
    @Override
    public void deleteUserById(int id) {
        if (checkId(id)) {
            userDao.deleteById(id);
        }
    }

    /**
     * Récupère un utilisateur à partir de son adresse e-mail.
     *
     * @param email l’adresse e-mail recherchée.
     * @return le {@link UserDTO} correspondant ou {@code null} si non trouvé.
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        if (checkEmail(email)) {
            return mapper.fromUserToDtoWithoutPassword(userDao.findByEmail(email));
        } else {
            return null;
        }
    }

    /**
     * Récupère un utilisateur à partir de son nom d’utilisateur.
     *
     * @param username le nom d’utilisateur.
     * @return le {@link UserDTO} correspondant ou {@code null} si non trouvé.
     */
    @Override
    public UserDTO getUserByUsername(String username) {
        if (this.checkUsername(username)) {
            return mapper.fromUserToDtoWithoutPassword(userDao.findByUsername(username));
        } else {
            return null;
        }
    }

    /**
     * Enregistre un nouvel utilisateur dans la base de données avec chiffrement du mot de passe.
     *
     * @param userDto les informations du nouvel utilisateur.
     * @return le {@link UserDTO} enregistré (sans mot de passe).
     */
    @Override
    public UserDTO saveUser(UserDTO userDto) {
        User user = mapper.fromDtoToUser(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDao.save(user);
        return mapper.fromUserToDtoWithoutPassword(userDao.findById(userDto.getId()));
    }

    /**
     * Met à jour les informations d’un utilisateur existant.
     *
     * @param userDto les nouvelles informations de l’utilisateur.
     * @return le {@link UserDTO} mis à jour (sans mot de passe).
     */
    @Override
    public UserDTO updateUser(UserDTO userDto) {
        UserDTO userToUpdate = mapper.fromUserToDto(userDao.findById(userDto.getId()));
        if (!userDto.getPassword().isEmpty()) {
            userToUpdate.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setName(userDto.getName());

        userDao.save(mapper.fromDtoToUser(userToUpdate));
        return mapper.fromUserToDtoWithoutPassword(userDao.findById(userDto.getId()));
    }

    /**
     * Vérifie si une adresse e-mail est déjà associée à un utilisateur.
     *
     * @param email l’adresse e-mail à vérifier.
     * @return {@code true} si l’e-mail existe, sinon {@code false}.
     */
    private boolean checkEmail(String email) {
        return userDao.findByEmail(email) != null;
    }

    /**
     * Vérifie si un nom d’utilisateur existe déjà.
     *
     * @param username le nom d’utilisateur à vérifier.
     * @return {@code true} si le nom existe, sinon {@code false}.
     */
    private boolean checkUsername(String username) {
        return userDao.findByUsername(username) != null;
    }

    /**
     * Vérifie si un utilisateur existe via son identifiant.
     *
     * @param id l’identifiant à vérifier.
     * @return {@code true} si l’utilisateur existe, sinon {@code false}.
     */
    @Override
    public boolean checkId(Integer id) {
        return userDao.findById(id).isPresent();
    }

    /**
     * Ajoute un thème à la liste des thèmes suivis par un utilisateur.
     *
     * @param user    l’utilisateur concerné.
     * @param themeId l’identifiant du thème à suivre.
     * @return le {@link UserDTO} mis à jour.
     * @throws ValidationException si l’utilisateur n’existe pas ou en cas d’erreur.
     */
    @Override
    public UserDTO addThemeToFollowed(UserDTO user, int themeId) {
        if (this.checkEmail(user.getEmail())) {
            List<ThemeDTO> themes = user.getFollowedThemes();
            themes.add(themeService.getThemeById(themeId));
            user.setFollowedThemes(themes);
            userDao.save(mapper.fromDtoToUser(user));
            return mapper.fromUserToDtoWithoutPassword(userDao.findById(user.getId()));
        } else {
            throw new ValidationException("error while adding theme");
        }
    }

    /**
     * Supprime un thème de la liste des thèmes suivis par un utilisateur.
     *
     * @param user    l’utilisateur concerné.
     * @param themeId l’identifiant du thème à retirer.
     * @return le {@link UserDTO} mis à jour.
     * @throws ValidationException si l’utilisateur n’existe pas ou en cas d’erreur.
     */
    @Override
    public UserDTO removeThemeToFollowed(UserDTO user, int themeId) {
        if (this.checkEmail(user.getEmail())) {
            List<ThemeDTO> themes = user.getFollowedThemes();
            themes.remove(themeService.getThemeById(themeId));
            user.setFollowedThemes(themes);
            userDao.save(mapper.fromDtoToUser(user));
            return mapper.fromUserToDtoWithoutPassword(userDao.findById(user.getId()));
        } else {
            throw new ValidationException("error while adding theme");
        }
    }
}
