package com.openclassrooms.mddapi.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

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

    @Column(name="password")
    private String password;
}
