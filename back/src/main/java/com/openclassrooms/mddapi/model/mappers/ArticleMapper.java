package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper MapStruct permettant la conversion entre l’entité {@link Article}
 * et son équivalent Data Transfer Object {@link ArticleDTO}.
 * <p>
 * Cette interface est utilisée pour automatiser la transformation des données
 * entre les couches de l’application (ex. : entité vers DTO pour exposition via API REST
 * et inversement pour la persistance).
 * </p>
 *
 * <p>
 * Annotée avec {@link Mapper}, cette interface est gérée par Spring grâce au paramètre
 * {@code componentModel = SPRING}. Les dépendances vers d’autres mappers
 * ({@link ThemeMapper}, {@link CommentMapper}, {@link UserMapper}) permettent
 * d’effectuer des conversions imbriquées si nécessaire.
 * </p>
 *
 * <p>
 * La stratégie {@link ReportingPolicy#IGNORE} indique que les propriétés non mappées
 * ne génèrent pas d’erreur de compilation.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Component
@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ThemeMapper.class, CommentMapper.class, UserMapper.class}
)
public interface ArticleMapper {

    /**
     * Convertit un objet {@link ArticleDTO} en entité {@link Article}.
     *
     * @param articleDTO le DTO représentant un article.
     * @return une entité {@link Article} correspondante.
     */
    Article fromDtoToArticle(ArticleDTO articleDTO);

    /**
     * Convertit une entité {@link Article} en objet {@link ArticleDTO}.
     *
     * @param article l’entité représentant un article.
     * @return un DTO {@link ArticleDTO} correspondant.
     */
    ArticleDTO fromArticleToDto(Article article);
}
