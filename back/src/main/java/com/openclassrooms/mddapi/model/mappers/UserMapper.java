package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ThemeMapper.class}
)
public interface UserMapper {

    /** Mappe un DTO vers une entité User */
    User fromDtoToUser(UserDTO dto);

    /** Mappe une entité User vers un DTO complet */
    UserDTO fromUserToDto(User entity);

    /**
     * ✅ Mappe un User vers un UserDTO
     * en ignorant le champ "password"
     */
    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserDTO fromUserToDtoWithoutPassword(User entity);
}
