package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Article;
import com.openclassrooms.mddapi.model.mappers.ArticleMapper;
import com.openclassrooms.mddapi.repositories.ArticleDAO;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    @Qualifier("articleMapper")
    private final ArticleMapper mapper;

    private final UserService userService;
    private final ThemeService themeService;

    @Override
    public List<ArticleDTO> getArticles(){
        List<Article> articles = articleDAO.findAll();
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        return articlesDTO;
    }

    @Override
    public ArticleDTO save(int themeId, String content, int userId){
        UserDTO author = userService.getUserById(userId);
        ThemeDTO theme = themeService.getThemeById(themeId);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTheme(theme);
        articleDTO.setContent(content);
        articleDTO.setAuthor(author);

        return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleDTO)));

    }
}
