package com.openclassrooms.mddapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleDTO   implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private ThemeDTO theme;
    private String content;
    private UserDTO author;

}
