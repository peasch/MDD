package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.dto.UserDTO;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.openclassrooms.mddapi.config.Constants.*;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
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
        model.put("themes", themeService.getAllThemes());

        return ok(model);
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/follow/{id}")
    public ResponseEntity<Map<Object, Object>> follow(@PathVariable(name = "id") int themeId,
                                                      @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {
            UserDTO userUpdated = userService.addThemeToFollowed(userLoggedIn, themeId);

            model.put("user", userUpdated);

            log.info("Followed theme !" + userUpdated.toString());
            return ok(model);
        } catch (Exception e) {
            model.put(MESSAGE, SOMETHING_WRONG_FOLLOW);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }

    @Operation(summary = "all articles method", description = "get all articles in database")
    @ApiResponse(responseCode = "200", description = "request ok")
    @ApiResponse(responseCode = "500", description = "error")
    @PostMapping("/unfollow/{id}")
    public ResponseEntity<Map<Object, Object>> unFollow(@PathVariable(name = "id") int themeId,
                                                        @AuthenticationPrincipal Jwt principal) {

        UserDTO userLoggedIn = userService.getUserByEmail(principal.getClaimAsString("sub"));
        Map<Object, Object> model = new HashMap<>();
        try {

            UserDTO userUpdated = userService.removeThemeToFollowed(userLoggedIn, themeId);

            model.put("user", userUpdated);

            log.info("unfollowed theme !" + userUpdated.toString());
            return ok(model);
        } catch (Exception e) {
            model.put(MESSAGE, SOMETHING_WRONG_FOLLOW);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(model);
        }
    }


}
