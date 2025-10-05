package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
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
    public List<CommentDTO> getAllCommentsOfArticle(int id) {
        if (articleService.checkArticle(id)) {
            List<Comment> comments = commentDAO.findAllByArticle_id(id);
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        } else {
            throw new NoSuchElementException("Article with id " + id + " not found");
        }


    }

    @Override
    public List<CommentDTO> getAllCommentsOfUser(int id) {
        if (userService.checkId(id)) {
            UserDTO author = userService.getUserById(id);
            List<Comment> comments = commentDAO.findAllByAuthor(userMapper.fromDtoToUser(author));
            return checkNoComments(comments, author.getId());
        } else {
            throw new NoSuchElementException("User with id : " + id + " not found");
        }

    }

    @Override
    public List<CommentDTO> getAllCommentsOfUserAndArticle(int userId, int articleId) {
        if (userService.checkId(userId) && articleService.checkArticle(articleId)) {
            UserDTO author = userService.getUserById(userId);
            List<Comment> comments = commentDAO.findAllByAuthorAndArticle_id(userMapper.fromDtoToUser(author), articleId);
            return checkNoCommentsOrArticle(comments, userId, articleId);
        } else {
            throw new NoSuchElementException("User " + userId + " or article " + articleId + " not found");
        }
    }

    @Override
    public CommentDTO addCommentToArticle(int articleId, int userId, String content){
        CommentDTO commentDTO = new CommentDTO();
        if (userService.checkId(userId) && articleService.checkArticle(articleId)) {
            UserDTO author = userService.getUserById(userId);
            ArticleDTO articleDTO =articleService.getArticle(articleId);
            commentDTO.setAuthor(author);
            commentDTO.setArticle(articleDTO);
            commentDTO.setContent(content);
            return mapper.fromCommentToCommentDTO(commentDAO.save(mapper.fromDtoToComment(commentDTO)));
        }else {
            throw new NoSuchElementException("Article with id " + articleId + " not found");
        }
    }

    public List<CommentDTO> checkNoComments(List<Comment> comments, int userId) {
        if (comments.isEmpty()) {
            throw new NoSuchElementException("User with id : " + userId + "has no comments");
        } else {
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        }

    }

    public List<CommentDTO> checkNoCommentsOrArticle(List<Comment> comments, int userId, int articleId) {
        if (comments.isEmpty()) {
            throw new NoSuchElementException("User with id: " + userId + " has no comments on article: " + articleId);
        } else {
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(comment -> commentDTOS.add(mapper.fromCommentToCommentDTO(comment)));
            return commentDTOS;
        }

    }

}
