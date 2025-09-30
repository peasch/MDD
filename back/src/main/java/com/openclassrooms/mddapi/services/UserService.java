package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.dto.UserDTO;

public interface UserService {
    UserDTO getUserByEmail(String email);

    UserDTO saveUser(UserDTO userDto);

    UserDTO getUserById(int id);

    void deleteUserById(int id);

}
