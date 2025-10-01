package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import org.hibernate.annotations.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import javax.swing.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    Article fromDtoToArticle(ArticleDTO articleDTO);

    @Mapping(target="ThemeDTO",source = "Theme")
    ArticleDTO  fromArticleToDto(Article article);
}
