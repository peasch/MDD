package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ThemeMapper.class,CommentMapper.class})

public interface ArticleMapper {

    Article fromDtoToArticle(ArticleDTO articleDTO);

    ArticleDTO  fromArticleToDto(Article article);

}
