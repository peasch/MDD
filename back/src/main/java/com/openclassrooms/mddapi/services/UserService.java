package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;

public interface UserService {

    UserDTO getUserByEmail(String email);

    UserDTO getUserByUsername(String username);

    UserDTO saveUser(UserDTO userDto);

    UserDTO getUserById(int id);

    void deleteUserById(int id);

    boolean checkId(Integer id);

    UserDTO addThemeToFollowed(UserDTO user, int themeId);
    UserDTO removeThemeToFollowed(UserDTO user, int themeId);
}
