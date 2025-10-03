package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDAO  extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser_Id(int id);
    List<Comment> findAllByArticle_Id(int id);
}
