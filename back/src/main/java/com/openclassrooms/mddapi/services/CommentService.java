package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();

    List<CommentDTO> getAllCommentsOfArticle(int id);


    List<CommentDTO> getAllCommentsOfUser(int id);

}
