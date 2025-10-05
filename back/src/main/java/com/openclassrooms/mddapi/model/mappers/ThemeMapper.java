package com.openclassrooms.mddapi.model.mappers;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.entities.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThemeMapper {

    Theme  fromDtoToTheme(ThemeDTO themeDTO);

    @Named("withoutfollowers")
    @Mapping(target = "followers", ignore = true)
    ThemeDTO  fromThemeToDto(Theme theme);
}
