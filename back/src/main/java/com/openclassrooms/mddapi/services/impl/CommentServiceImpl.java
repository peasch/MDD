package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.entities.Comment;
import com.openclassrooms.mddapi.model.mappers.CommentMapper;
import com.openclassrooms.mddapi.repositories.CommentDAO;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    @Qualifier("commentMapper")
    private final CommentMapper mapper;
    private final UserService userService;
    private final ThemeService themeService;
    private final CommentDAO  commentDAO;

    @Override
    public List<CommentDTO> getAllComments(){
        List<Comment> comments = commentDAO.findAll();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> getAllCommentsOfArticle(int articleId){
        List<Comment> comments = commentDAO.findAllByArticle_Id(articleId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
        return  commentDTOS;
    }

    @Override
    public void deleteAllCommentsOfArticle(int articleId){
        List<Comment> comments = commentDAO.findAllByArticle_Id(articleId);
        commentDAO.deleteAll(comments);
    }
}
