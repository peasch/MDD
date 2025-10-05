package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Comment;
import com.openclassrooms.mddapi.model.mappers.CommentMapper;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.CommentDAO;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    @Qualifier("commentMapper")
    private final CommentMapper mapper;
    private final UserService userService;
    private final CommentDAO commentDAO;
    private final ArticleService articleService;

    @Qualifier("userMapper")
    private final UserMapper userMapper;

    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentDAO.findAll();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> getAllCommentsOfArticle(int id){
            if (articleService.checkArticle(id)) {
            List<Comment> comments = commentDAO.findAllByArticle_id(id);
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        }else{
                throw new NoSuchElementException("Article with id " + id + " not found");
        }


    }

    @Override
    public List<CommentDTO> getAllCommentsOfUser(int id){
        if (userService.checkId(id)){
            UserDTO author = userService.getUserById(id);
            List<Comment> comments = commentDAO.findAllByAuthor(userMapper.fromDtoToUser(author));
            if(comments.isEmpty()){
                throw new NoSuchElementException("User with id : " + id + "has no comments");
            }else {
                List<CommentDTO> commentDTOS = new ArrayList<>();
                comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
                return commentDTOS;
            }
        }else{
            throw new NoSuchElementException("User with id : " + id + " not found");
        }

    }
//


}
