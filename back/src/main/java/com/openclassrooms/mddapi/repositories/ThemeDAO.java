package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.model.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeDAO  extends JpaRepository<Theme, Integer> {


}
