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
        List<Article> articles = articleDAO.findArticlesByTheme_Id(id);
        List<ArticleDTO> articlesDTO = new ArrayList<>();
        articles.forEach(article -> articlesDTO.add(mapper.fromArticleToDto(article)));
        return articlesDTO;
    }

    @Override
    public ArticleDTO save(int themeId, String content, int userId) {
        UserDTO author = userService.getUserById(userId);
        ThemeDTO theme = themeService.getThemeById(themeId);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTheme(theme);
        articleDTO.setContent(content);
        articleDTO.setAuthor(author);
        articleDTO.setCreatedAt(new Date());
        return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleDTO)));

    }

    @Override
    public ArticleDTO updateArticle(int id, ArticleDTO articleDTO, UserDTO userDTO) {

        ArticleDTO articleToUpdate = mapper.fromArticleToDto(articleDAO.findById(id));
        if (userDTO.getEmail().equals(articleDTO.getAuthor().getEmail())) {
            articleToUpdate.setContent(articleDTO.getContent());
            articleToUpdate.setAuthor(articleDTO.getAuthor());
            articleToUpdate.setTheme(articleDTO.getTheme());
            articleToUpdate.setUpdatedAt(new Date());
            return mapper.fromArticleToDto(articleDAO.save(mapper.fromDtoToArticle(articleToUpdate)));
        } else {
            throw new NoSuchElementException("vous n'êtes pas l'auteur de l'article");
        }
    }

    @Override
    public ArticleDTO getArticle(int id) {
        if (checkArticle(id)) {
            return mapper.fromArticleToDto(articleDAO.findById(id));
        } else {
            throw new NoSuchElementException("Article with id " + id + " not found");
        }
    }

    @Override
    public void deleteArticle(int id, UserDTO userDTO) {
        ArticleDTO articleToDelete = mapper.fromArticleToDto(articleDAO.findById(id));
        if (articleDAO.existsById(id) && userDTO.getEmail().equals(articleToDelete.getAuthor().getEmail())) {
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
    public List<ArticleDTO> getAllFollowedArticles(UserDTO user){
        List<ThemeDTO> followedTheme = user.getFollowedThemes();
        List<ArticleDTO> followedArticlesOfTheme = new ArrayList<>();
        List<ArticleDTO> followedArticles = new ArrayList<>();
        for(ThemeDTO themeDTO : followedTheme) {
            followedArticlesOfTheme = this.getAllArticlesOfTheme(themeDTO.getId());
            followedArticles.addAll(followedArticlesOfTheme);

        }
        return followedArticles;
    }
}
