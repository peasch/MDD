package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.DTO.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.entities.User;

import java.util.List;

public interface ArticleService {

    ArticleDTO getArticleById(int id);

    ArticleDTO saveArticle(ArticleDTO articleDto);
    List<Article> getAllArticles();

    ArticleDTO save(Theme theme, String content, User author);
}
