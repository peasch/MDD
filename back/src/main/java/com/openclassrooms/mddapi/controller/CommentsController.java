package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.CommentDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.ok;
import static com.openclassrooms.mddapi.config.Constants.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/")
@Tag(name = "Comments Controller", description = "comments actions services")
public class CommentsController {

    private final UserService userService;
    private final CommentService commentService;

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getComments() {
        Map<Object, Object> model = new HashMap<>();
        model.put(COMMENTS, commentService.getAllComments());
        model.put(MESSAGE, "All comments !");
        return ok(model);
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getCommentsOfArticleId(@PathVariable(name = "id") int id) {
        Map<Object, Object> model = new HashMap<>();
        try {
            model.put(COMMENTS, commentService.getAllCommentsOfArticle(id));
            model.put(MESSAGE, "Comments of article id :" + id);
            return ok(model);
        } catch (NoSuchElementException _) {
            model.put(MESSAGE, "article not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/author/{id}")
    public ResponseEntity<Map<Object, Object>> getCommentsOfUserId(@PathVariable(name = "id") int id) {
        Map<Object, Object> model = new HashMap<>();
        model.put("user", id);
        try {
            model.put(COMMENTS, commentService.getAllCommentsOfUser(id));
            model.put(MESSAGE, "Comments of User id :" + id);
            log.info("get comments of user id :"  );
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/user/article/")
    public ResponseEntity<Map<Object, Object>> getCommentsOfUserIdAndArticleId(@AuthenticationPrincipal Jwt principal,
                                                                               @Valid @RequestParam("article") int articleId) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        int userId = userLoggedIn.getId();
        Map<Object, Object> model = new HashMap<>();
        model.put("user", userId);
        model.put("article", articleId);
        try {
            model.put(COMMENTS, commentService.getAllCommentsOfUserAndArticle(userId, articleId));
            model.put(MESSAGE, "Comments of User id :" + userId);
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/add/{id}")
    public ResponseEntity<Map<Object, Object>> addCommentToArticle(@AuthenticationPrincipal Jwt principal,
                                                                   @PathVariable(name = "id") int articleId,
                                                                   @Valid @RequestParam("content") String content) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        int userId = userLoggedIn.getId();
        Map<Object, Object> model = new HashMap<>();
        model.put("user", userId);
        model.put("article", articleId);
        try{
            CommentDTO commentDTO = commentService.addCommentToArticle(articleId, userId, content);
            model.put("comment saved", commentDTO);
            model.put(MESSAGE, "comment added");
            return ok(model);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            model.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
        }

    }



}
