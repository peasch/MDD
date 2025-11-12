package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;

import java.util.List;

/**
 * Service définissant les opérations métier liées à la gestion des thèmes.
 * <p>
 * Cette interface regroupe les principales fonctionnalités permettant de :
 * <ul>
 *     <li>Récupérer un thème spécifique à partir de son identifiant,</li>
 *     <li>Récupérer la liste complète de tous les thèmes disponibles.</li>
 * </ul>
 * </p>
 *
 * <p>
 * L’implémentation par défaut de cette interface est
 * {@link com.openclassrooms.mddapi.services.impl.ThemeServiceImpl}.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
public interface ThemeService {

    /**
     * Récupère un thème à partir de son identifiant unique.
     *
     * @param id l’identifiant du thème.
     * @return le {@link ThemeDTO} correspondant, ou {@code null} si le thème n’existe pas.
     */
    ThemeDTO getThemeById(Integer id);

    /**
     * Récupère la liste de tous les thèmes existants dans la base de données.
     *
     * @return une liste de {@link ThemeDTO}.
     */
    List<ThemeDTO> getAllThemes();
}
