package com.openclassrooms.mddapi.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name ="comment",schema ="MDD")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name ="content")
    private String content;


    @JoinColumn(name = "user_id")
    private int authorId;

    @JoinColumn(name = "article_id")
    private int articleId;


    @Column(name="created_at")
    private Date createdAt;

}
