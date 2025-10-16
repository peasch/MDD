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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "content", length = 2500)
    @Size(max = 2500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author ;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updateAt")
    private Date updatedAt;
}
