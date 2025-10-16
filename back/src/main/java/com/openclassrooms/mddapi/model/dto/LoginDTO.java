package com.openclassrooms.mddapi.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDTO {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

}
