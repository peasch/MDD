package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDAO  extends JpaRepository<Comment, Long> {
}
