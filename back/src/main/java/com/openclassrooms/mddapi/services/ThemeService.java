package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;

import java.util.List;

public interface ThemeService {
    ThemeDTO getThemeById(Integer id);

    List<ThemeDTO> getAllThemes();



}
