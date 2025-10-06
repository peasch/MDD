package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ThemeMapper.class})
public interface UserMapper {


    User fromDtoToUser(UserDTO dto) ;

    UserDTO fromUserToDto(User dto) ;


}