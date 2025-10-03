package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.mappers.ThemeMapper;
import com.openclassrooms.mddapi.repositories.ThemeDAO;
import com.openclassrooms.mddapi.services.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeDAO  themeDAO;
    private final ThemeMapper themeMapper;
    @Override
    public ThemeDTO getThemeById(Integer id) {
        if(themeDAO.findById(id).isPresent()){
            return themeMapper.fromThemeToDto(themeDAO.findById(id).get());
        }else{
            return null;
        }

    }
}
