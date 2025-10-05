package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.User;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.repositories.UserDAO;
import com.openclassrooms.mddapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDAO userDao;

    @Qualifier("userMapper")
    private final UserMapper mapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public UserDTO getUserById(int id) {
        return mapper.fromUserToDto(userDao.findById(id));
    }

    @Override
    public void deleteUserById(int id) {
        if (checkId(id)) {
            userDao.deleteById(id);
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        if (checkEmail(email)) {
            return mapper.fromUserToDto(userDao.findByEmail(email));
        }else{
            return null;
        }
    }

    @Override
    public UserDTO saveUser(UserDTO userDto) {
        if (!this.checkEmail(userDto.getEmail())) {
            User user = mapper.fromDtoToUser(userDto);
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userDao.save(user);
            return mapper.fromUserToDto(userDao.findById(userDto.getId()));
        } else {
            throw new ValidationException("already used email");
        }


    }
    private boolean checkEmail(String email) {
        return userDao.findByEmail(email) != null;

    }
    @Override
    public boolean checkId(Integer id) {
        return userDao.findById(id).isPresent();
    }

    @Override
    public UserDTO addThemeToFollowed(UserDTO user, ThemeDTO theme){
        if (this.checkEmail(user.getEmail())) {
            List<ThemeDTO> themes = user.getFollowedThemes();
            themes.add(theme);
            user.setFollowedThemes(themes);
            userDao.save(mapper.fromDtoToUser(user));
            return mapper.fromUserToDto(userDao.findById(user.getId()));
        } else {
            throw new ValidationException("error while adding theme");
        }

    }
}
