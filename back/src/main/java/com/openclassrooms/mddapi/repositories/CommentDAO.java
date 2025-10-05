package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Comment;
import com.openclassrooms.mddapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDAO  extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthor(User author);
    List<Comment> findAllByArticle_id(int id);
    List<Comment> findAllByAuthorAndArticle_id(User author, int id);
}
