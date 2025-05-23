package com.openclassrooms.mddapi.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String email;

    private String password;

}
