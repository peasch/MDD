package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDAO extends JpaRepository<Article, Integer> {
}
