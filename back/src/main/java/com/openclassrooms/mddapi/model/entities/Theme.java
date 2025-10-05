package com.openclassrooms.mddapi.model.entities;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name="theme",schema="MDD")
public class Theme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name ="name")
    private String name;


    @ManyToMany(mappedBy = "followedThemes")
    private List<User> followers;
}
