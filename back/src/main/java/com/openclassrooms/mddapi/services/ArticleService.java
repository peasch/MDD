package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getArticles();

    List<ArticleDTO> getAllArticlesOfTheme(int id);

    ArticleDTO save(ArticleDTO article);

    ArticleDTO updateArticle(int id, ArticleDTO articleDTO, UserDTO userDTO);

    ArticleDTO getArticle(int id);

    void deleteArticle(int id, UserDTO userDTO);

    boolean checkArticle(int id);

    List<ArticleDTO> getAllFollowedArticles(UserDTO user);
}
