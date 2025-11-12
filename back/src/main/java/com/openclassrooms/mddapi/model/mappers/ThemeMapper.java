package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper MapStruct responsable de la conversion entre l’entité {@link Theme}
 * et son équivalent Data Transfer Object {@link ThemeDTO}.
 * <p>
 * Ce mapper facilite la transformation des données entre la couche de persistance (entité)
 * et la couche de présentation (DTO), permettant une isolation claire des modèles.
 * </p>
 *
 * <p>
 * Annotée avec {@link Mapper}, cette interface est intégrée à Spring via
 * {@code componentModel = SPRING}. La stratégie {@link ReportingPolicy#IGNORE}
 * permet d’ignorer les champs non mappés sans générer d’erreur.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Component
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThemeMapper {

    /**
     * Convertit un objet {@link ThemeDTO} en entité {@link Theme}.
     *
     * @param themeDTO le DTO représentant un thème.
     * @return l’entité {@link Theme} correspondante.
     */
    Theme fromDtoToTheme(ThemeDTO themeDTO);

    /**
     * Convertit une entité {@link Theme} en objet {@link ThemeDTO}.
     *
     * @param theme l’entité représentant un thème.
     * @return le DTO {@link ThemeDTO} correspondant.
     */
    ThemeDTO fromThemeToDto(Theme theme);
}
