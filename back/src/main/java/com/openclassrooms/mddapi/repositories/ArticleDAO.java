package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleDAO extends JpaRepository<Article, Integer> {
    Article findById(int id);
    Article findByTitle(String title);
    Article findByAuthor(String author);
    Article findByTheme(Integer theme);

    @Override
    List<Article> findAll();
}
