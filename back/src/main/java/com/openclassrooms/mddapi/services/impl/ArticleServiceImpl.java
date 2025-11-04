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
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    @Qualifier("articleMapper")
    private final ArticleMapper mapper;

    private final UserService userService;
    private final ThemeService themeService;

    @Override
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleDAO.findAll();
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        return articlesDTO;
    }

    @Override
    public List<ArticleDTO> getAllArticlesOfTheme(int id) {
        List<Article> articles = articleDAO.findArticlesByThemeId(id);
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        articlesDTO.forEach(article->article.setAuthorUsername(userService.getUserById(article.getAuthorId()).getUsername()));
        return articlesDTO;
    }

    @Override
    public ArticleDTO save(ArticleDTO article) {
        UserDTO author = userService.getUserById(article.getAuthorId());
        ThemeDTO theme = themeService.getThemeById(article.getThemeId());
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setThemeId(theme.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setAuthorId(author.getId());

        articleDTO.setCreatedAt(new Date());
        return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleDTO)));

    }

    @Override
    public ArticleDTO updateArticle(int id, ArticleDTO articleDTO, UserDTO userDTO) {

        ArticleDTO articleToUpdate = mapper.fromArticleToDto(articleDAO.findById(id));
        UserDTO articleUser = userService.getUserById(articleDTO.getAuthorId());
        if (userDTO.getEmail().equals(articleUser.getEmail())) {
            articleToUpdate.setContent(articleDTO.getContent());
            articleToUpdate.setAuthorId(articleDTO.getAuthorId());
            articleToUpdate.setThemeId(articleDTO.getThemeId());
            articleToUpdate.setUpdatedAt(new Date());
            return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleToUpdate)));
        } else {
            throw new NoSuchElementException("vous n'êtes pas l'auteur de l'article");
        }
    }

    @Override
    public ArticleDTO getArticle(int id) {
        if (checkArticle(id)) {
            ArticleDTO article = mapper.fromArticleToDto(articleDAO.findById(id));
            article.setAuthorUsername(userService.getUserById(article.getAuthorId()).getUsername());
            return article;
        } else {
            throw new NoSuchElementException("Article with id " + id + " not found");
        }
    }

    @Override
    public void deleteArticle(int id, UserDTO userDTO) {
        ArticleDTO articleToDelete = mapper.fromArticleToDto(articleDAO.findById(id));
        UserDTO articleUser = userService.getUserById(articleToDelete.getAuthorId());
        if (articleDAO.existsById(id) && userDTO.getEmail().equals(articleUser.getEmail())) {
            articleDAO.deleteById(id);
        } else {
            throw new NoSuchElementException("vous n'êtes pas l'auteur de l'article");
        }
    }

    @Override
    public boolean checkArticle(int id) {
        return articleDAO.existsById(id);
    }

    @Override
    public List<ArticleDTO> getAllFollowedArticles(UserDTO user) {
        List<ThemeDTO> followedTheme = user.getFollowedThemes();
        List<ArticleDTO> followedArticlesOfTheme;
        List<ArticleDTO> followedArticles = new ArrayList<>();
        for (ThemeDTO themeDTO : followedTheme) {
            followedArticlesOfTheme = this.getAllArticlesOfTheme(themeDTO.getId());
            followedArticles.addAll(followedArticlesOfTheme);

        }
        return followedArticles;
    }
}
