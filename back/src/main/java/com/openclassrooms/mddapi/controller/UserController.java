package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.model.entities.User;
import com.openclassrooms.mddapi.model.mappers.UserMapper;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
@Tag(name = "User Controller", description = "all users services")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    @Operation(summary = "delete user", description = "delete then chosen user")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @DeleteMapping("{id}")
    public ResponseEntity<?> save(@PathVariable("id") String id) {
        try {
            UserDTO user = this.userService.getUserById(Integer.parseInt(id));

            if (user == null) {
                return ResponseEntity.notFound().build();
            }


            this.userService.deleteUserById(Integer.parseInt(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
