package com.openclassrooms.mddapi.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Table( name ="article", schema="MDD")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "content", length = 2500)
    @Size(max = 2500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author ;

    @OneToMany(mappedBy = "article")
    private Set<Comment> comments = new HashSet<>();
}
