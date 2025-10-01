package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getArticles();

    ArticleDTO save(int themeId, String content, int userId);
}
