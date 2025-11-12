package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper MapStruct permettant la conversion entre l’entité {@link Comment}
 * et son équivalent Data Transfer Object {@link CommentDTO}.
 * <p>
 * Cette interface définit les méthodes nécessaires pour transformer les objets
 * entre la couche de persistance (entités JPA) et la couche de transfert (DTO),
 * facilitant ainsi la communication entre la base de données et l’API REST.
 * </p>
 *
 * <p>
 * Annotée avec {@link Mapper}, elle est gérée automatiquement par Spring
 * grâce au paramètre {@code componentModel = SPRING}.
 * Les mappers associés ({@link ThemeMapper} et {@link UserMapper}) sont utilisés
 * pour convertir les relations éventuelles.
 * </p>
 *
 * <p>
 * La stratégie {@link ReportingPolicy#IGNORE} permet d’ignorer les champs non mappés
 * sans provoquer d’erreur de compilation.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Component
@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ThemeMapper.class, UserMapper.class}
)
public interface CommentMapper {

    /**
     * Convertit un objet {@link CommentDTO} en entité {@link Comment}.
     *
     * @param commentDTO le DTO représentant un commentaire.
     * @return l’entité {@link Comment} correspondante.
     */
    Comment fromDtoToComment(CommentDTO commentDTO);

    /**
     * Convertit une entité {@link Comment} en objet {@link CommentDTO}.
     *
     * @param comment l’entité représentant un commentaire.
     * @return le DTO {@link CommentDTO} correspondant.
     */
    CommentDTO fromCommentToCommentDTO(Comment comment);
}
