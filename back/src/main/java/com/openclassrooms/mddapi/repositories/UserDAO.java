package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO  extends JpaRepository<User, Integer> {
}
