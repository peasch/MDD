package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface
CommentDAO  extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthorId(int authorId);
    List<Comment> findAllByArticleId(int id);
    List<Comment> findAllByAuthorIdAndArticleId(int authorId, int articleId);
    Comment save(Comment comment);
}
