package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getArticles();

    List<ArticleDTO> getAllArticlesOfTheme(int id);

    ArticleDTO save(int themeId, String content, int userId);

    ArticleDTO updateArticle(int id,ArticleDTO articleDTO);

    ArticleDTO getArticle(int id);

    void deleteArticle(int id);

    boolean checkArticle(int id);
}
