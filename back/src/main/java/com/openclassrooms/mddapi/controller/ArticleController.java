package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.DTO.ArticleDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/")
@Tag(name = "Article Controller", description = "all about articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "here are your articles")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping
    public ResponseEntity<Map<Object, Object>> getAllArticles(){
        Map<Object, Object> model = Map.of("articles", articleService.getAllArticles());

        return ResponseEntity.ok(model);
    }

   /* public ResponseEntity<Map<Object, Object>> createArticle(@Valid @RequestParam(name = "content") String content,
                                                             @AuthenticationPrincipal Jwt principal){
        Integer authorId = userService.getUserByEmail(principal.getClaimAsString("sub")).getId();

        try{
            ArticleDTO article = articleService.saveArticle(new ArticleDTO(content, authorId));
        }

    }*/

}
