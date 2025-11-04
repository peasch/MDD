package com.openclassrooms.mddapi.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;


@Entity
@Data
@Table( name ="article", schema="MDD")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title", length = 100)
    @Size(max = 100)
    private String title;


    @Column(name = "theme_id")
    private int themeId;

    @Column(name = "content", length = 2500)
    @Size(max = 2500)
    private String content;


    @Column(name = "user_id")
    private Integer authorId ;



    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updateAt")
    private Date updatedAt;
}
