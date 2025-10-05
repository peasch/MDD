package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.ThemeDTO;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static com.openclassrooms.mddapi.config.Constants.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theme/")
@Tag(name = "theme Controller", description = "theme actions services")
public class ThemeController {

    private final ThemeService themeService;
    private final UserService userService;

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @GetMapping()
    public ResponseEntity<Map<Object, Object>> getThemes() {
        Map<Object, Object> model = new HashMap<>();
        model.put(COMMENTS, themeService.getAllThemes());
        model.put(MESSAGE, "All themes !");
        return ok(model);
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/follow/{id}")
    public ResponseEntity<Map<Object, Object>> follow(@PathVariable(name = "id") int themeId,
                                                      @AuthenticationPrincipal Jwt principal){

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            ThemeDTO themeDTO =themeService.getThemeById(themeId);
           UserDTO userUpdated =userService.addThemeToFollowed(userLoggedIn, themeDTO);
            model.put(THEME, themeService.addUserToThemeFollowers(userLoggedIn.getEmail(),themeDTO));
            model.put("user",userUpdated);
            model.put(MESSAGE, "Followed theme !");
            return ok(model);
        } catch (Exception _) {
            model.put(MESSAGE, SOMETHING_WRONG_FOLLOW);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }


}
