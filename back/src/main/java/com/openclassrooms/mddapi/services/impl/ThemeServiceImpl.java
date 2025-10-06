package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import com.openclassrooms.mddapi.model.entities.User;
import com.openclassrooms.mddapi.model.mappers.ThemeMapper;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.ThemeDAO;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeDAO themeDAO;
    private final ThemeMapper themeMapper;
    private final UserService userService;

    @Override
    public ThemeDTO getThemeById(Integer id) {
        if (themeDAO.findById(id).isPresent()) {
            return themeMapper.fromThemeToDto(themeDAO.findById(id).get());
        } else {
            return null;
        }

    }

    @Override
    public List<ThemeDTO> getAllThemes() {
        List<Theme> themes = themeDAO.findAll();
        List<ThemeDTO> themeDTOS = new ArrayList<>();
        themes.forEach(theme -> themeDTOS.add(themeMapper.fromThemeToDto(theme)));
        return themeDTOS;
    }



}
