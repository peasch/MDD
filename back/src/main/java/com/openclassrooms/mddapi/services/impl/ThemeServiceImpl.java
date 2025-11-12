package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.mappers.ThemeMapper;
import com.openclassrooms.mddapi.repositories.ThemeDAO;
import com.openclassrooms.mddapi.services.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service {@link ThemeService} pour la gestion des thèmes.
 * <p>
 * Cette classe fournit la logique métier liée aux thèmes, notamment :
 * <ul>
 *     <li>La récupération d’un thème par son identifiant,</li>
 *     <li>La récupération de la liste complète des thèmes disponibles.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Elle s’appuie sur :
 * <ul>
 *     <li>{@link ThemeDAO} pour la persistance des données,</li>
 *     <li>{@link ThemeMapper} pour la conversion entre entités et DTOs.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Annotée avec {@link Service}, cette classe est automatiquement gérée par Spring
 * et injectée là où le service {@link ThemeService} est requis.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ThemeServiceImpl implements ThemeService {

    /** DAO gérant les opérations de persistance sur les thèmes. */
    private final ThemeDAO themeDAO;

    /** Mapper responsable de la conversion entre entités {@link Theme} et DTOs {@link ThemeDTO}. */
    private final ThemeMapper themeMapper;

    /**
     * Récupère un thème à partir de son identifiant.
     *
     * @param id l’identifiant du thème recherché.
     * @return le {@link ThemeDTO} correspondant, ou {@code null} si le thème n’existe pas.
     */
    @Override
    public ThemeDTO getThemeById(Integer id) {
        if (themeDAO.findById(id).isPresent()) {
            return themeMapper.fromThemeToDto(themeDAO.findById(id).get());
        } else {
            return null;
        }
    }

    /**
     * Récupère la liste complète de tous les thèmes disponibles.
     *
     * @return une liste de {@link ThemeDTO} représentant tous les thèmes existants.
     */
    @Override
    public List<ThemeDTO> getAllThemes() {
        List<Theme> themes = themeDAO.findAll();
        List<ThemeDTO> themeDTOS = new ArrayList<>();
        themes.forEach(theme -> themeDTOS.add(themeMapper.fromThemeToDto(theme)));
        return themeDTOS;
    }
}
