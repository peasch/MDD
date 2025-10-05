package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeDAO  extends JpaRepository<Theme, Integer> {


}
