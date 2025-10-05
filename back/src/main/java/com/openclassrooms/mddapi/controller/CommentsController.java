package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/")
@Tag(name = "Comments Controller", description = "comments actions services")
public class CommentsController {
    private final ArticleService service;
    private final UserService userService;
    private final CommentService commentService;
    private static final String COMMENTS = "comments";
    private static final String  MESSAGE = "message";

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
        try {
            Map<Object, Object> model = new HashMap<>();
            model.put(COMMENTS, commentService.getAllCommentsOfArticle(id));
            model.put(MESSAGE, "Comments of article id :" + id);
            return ok(model);
        } catch (NoSuchElementException _) {
            Map<Object, Object> error = new HashMap<>();
            error.put(MESSAGE, "article not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/author/{id}")
    public ResponseEntity<Map<Object, Object>> getCommentsOfUserId(@PathVariable(name = "id") int id) {
        try {
            Map<Object, Object> model = new HashMap<>();
            model.put(COMMENTS, commentService.getAllCommentsOfUser(id));
            model.put(MESSAGE, "Comments of User id :" + id);
            return ok(model);
        } catch (NoSuchElementException e) {
            Map<Object, Object> error = new HashMap<>();
            error.put(MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


}
