package com.openclassrooms.mddapi.model.DTO;

import com.openclassrooms.mddapi.model.entities.Comment;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
public class ArticleDTO {


    private int id;

    private Theme theme;

    private String content;

    private User author ;

    private List<User> followers;

    private Set<Comment> comments = new HashSet<>();
}
