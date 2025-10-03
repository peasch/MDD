package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.LoginDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
@Tag(name = "Authentication Controller", description = "register and login service")
public class AuthController {

    public final JWTService jwtService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Operation(summary = "login method", description = "clic here to login in with your credentials, email & password")
    @ApiResponse(responseCode = "200", description = "You're logged in")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO logintDto) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logintDto.getEmail(), logintDto.getPassword()));

            UserDTO userLoggedin = userService.getUserByEmail(logintDto.getEmail());

            String token = jwtService.generateToken(userLoggedin);
            Map<Object, Object> model = new HashMap<>();
            model.put("message", "logged in");
            model.put("token", token);
            return ok(model);
        } catch (Exception e) {
            Map<Object, Object> model = new HashMap<>();
            model.put("message", "Bad Credentials");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }

    }

    @Operation(summary = "register method", description = "clic here to register, with your details")
    @ApiResponse(responseCode = "200", description = "Welcome in, you're registered")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userDto) {

        if(userService.getUserByEmail(userDto.getEmail()) == null) {
            try {
                userService.saveUser(userDto);
                UserDTO saved = userService.getUserByEmail(userDto.getEmail());
                Map<Object, Object> model = new HashMap<>();
                model.put("message", "User registered successfully");
                model.put("token", jwtService.generateToken(saved));
                return ok(model);
            } catch (Exception e) {
                Map<Object, Object> error = new HashMap<>();
                error.put("message", "error with registration");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
        }else {
            Map<Object, Object> error = new HashMap<>();
            error.put("message", "Email already registered");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @ApiResponse(responseCode = "200", description = "here are your informations")
    @ApiResponse(responseCode = "400", description = "error")
    @GetMapping("/me")
    public ResponseEntity getMe(@AuthenticationPrincipal Jwt principal) {
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "get your informations");
        model.put("mail", userService.getUserByEmail(principal.getClaimAsString("sub")));
        return ok(model);

    }


}
