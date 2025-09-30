package com.openclassrooms.mddapi.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table( name = "user", schema ="MDD")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name ="name")
    private String name;

    @Column(name="email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="password")
    private String password;

    @OneToMany
    private Set<Article> followedArticles = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Article> writtenArticles = new HashSet<>();
}
