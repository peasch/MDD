package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.DTO.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING,unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ArticleMapper {

    Article fromDtoToArticle(ArticleDTO dto);

    ArticleDTO fromArticleToDto(Optional<Article> article);
}
