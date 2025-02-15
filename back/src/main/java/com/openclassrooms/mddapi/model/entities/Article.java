package com.openclassrooms.mddapi.model.entities;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
@Table( name ="article", schema="MDD")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name ="theme_id")
    private Integer theme_id;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "author_id", nullable = false)
    private Integer author_id;
}
