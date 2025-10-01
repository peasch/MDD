package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Article;
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

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object,Object>> getArticles() {

        Map<Object, Object> model = new HashMap<>();
        model.put("articles", service.getArticles());
        return ok(model);
    }

    public ResponseEntity<Map<Object,Object>> createArticle(@Valid @RequestParam("theme")int themeId,
                                                            @Valid @RequestParam("content")String content,
                                                            @AuthenticationPrincipal Jwt principal) {

        Integer authorId = userService.getUserByEmail(principal.getClaimAsString("sub")).getId();
        try {
            ArticleDTO articleDTO = service.save(themeId,content,authorId);
            Map<Object, Object> model = new HashMap<>();
            model.put("message", "Rental created !");
            model.put("rental", articleDTO);
            return ok(model);
        } catch (Exception e) {
            return new ResponseEntity("problem with rental", HttpStatus.UNAUTHORIZED);
        }
    }
}
