package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.DTO.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.entities.User;
import com.openclassrooms.mddapi.model.mappers.ArticleMapper;
import com.openclassrooms.mddapi.repositories.ArticleDAO;
import com.openclassrooms.mddapi.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;
    @Qualifier("articleMapper")
    private final ArticleMapper mapper;

    public List<Article> getAllArticles(){
        return articleDAO.findAll();
    }

    @Override
    public ArticleDTO save(Theme theme, String content, User author) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTheme(theme);
        articleDTO.setContent(content);
        articleDTO.setAuthor(author);
        return mapper.fromArticleToDto(Optional.of(articleDAO.save(mapper.fromDtoToArticle(articleDTO))));
    }

    @Override
    public ArticleDTO getArticleById(int id) {
        return mapper.fromArticleToDto(Optional.ofNullable(articleDAO.findById(id)));

    }

    @Override
    public ArticleDTO saveArticle(ArticleDTO articleDto) {
        Article article = mapper.fromDtoToArticle(articleDto);
        articleDAO.save(article);
        return mapper.fromArticleToDto(Optional.ofNullable(articleDAO.findById(article.getId())));
    }



}
