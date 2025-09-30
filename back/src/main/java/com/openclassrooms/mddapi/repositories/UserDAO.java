package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO  extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findById(int id);

    List<User> findAll();

    void deleteById(Integer id);
}
