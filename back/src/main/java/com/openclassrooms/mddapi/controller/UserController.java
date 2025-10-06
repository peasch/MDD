package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.openclassrooms.mddapi.config.Constants.MESSAGE;
import static com.openclassrooms.mddapi.config.Constants.THEMES;
import static org.springframework.http.ResponseEntity.ok;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
@Tag(name = "User Controller", description = "all users services")
public class UserController {

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
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping("/followed/")
    public ResponseEntity<Map<Object, Object>> getfollowedThemeOfUser(@AuthenticationPrincipal Jwt principal) {

        Map<Object, Object> model = new HashMap<>();
        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        model.put(THEMES, userLoggedIn.getFollowedThemes());
        model.put(MESSAGE, "All themes of user:" + userLoggedIn.getEmail());
        log.info(userLoggedIn.getEmail());
        return ok(model);
    }
}
