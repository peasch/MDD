package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/")
@Tag(name = "Articles Controller", description = "articles actions services")
public class ArticleController {

    private final ArticleService service;
    private final UserService userService;
    private static final String SOMETHING_WRONG = "something went wrong with this article";
    private static final String MESSAGE = "message";

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getArticles() {
        Map<Object, Object> model = new HashMap<>();
        model.put("articles", service.getArticles());
        return ok(model);
    }

    @Operation(summary = "submit an article method", description = "method to save an article")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/add")
    public ResponseEntity<Map<Object, Object>> createArticle(@Valid @RequestParam("theme") int themeId,
                                                             @Valid @RequestParam("content") String content,
                                                             @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            ArticleDTO articleDTO = service.save(themeId, content, userLoggedIn.getId());
            model.put(MESSAGE, "Article created !");
            model.put("article", articleDTO);
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<Object, Object>> updateArticle(@PathVariable(name = "id") int id, @Valid @RequestBody ArticleDTO articleDTO) {
        Map<Object, Object> model = new HashMap<>();

        try {
            ArticleDTO articleUpdatedDTO = service.updateArticle(id, articleDTO);
            model.put(MESSAGE, "Article updated!");
            model.put("article", articleUpdatedDTO);
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/theme/{id}")
    public ResponseEntity<Map<Object, Object>> getAllArticlesOfTheme(@PathVariable(name = "id") int themeId) {
        Map<Object, Object> model = new HashMap<>();

        try {
            List<ArticleDTO> articleDTOs = service.getAllArticlesOfTheme(themeId);
            model.put("articles", articleDTOs);
            model.put(MESSAGE, "articles found!");
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }


    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @DeleteMapping({"delete/{id}"})
    public ResponseEntity<Map<Object, Object>> deleteArticle(@PathVariable(name = "id") int id) {
        Map<Object, Object> model = new HashMap<>();
        try {
            service.deleteArticle(id);
            model.put(MESSAGE, "Article deleted!");
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

}
