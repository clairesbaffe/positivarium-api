package com.positivarium.api.controller;

import com.positivarium.api.dto.*;
import com.positivarium.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/")
    public UserWithRolesDTO getOwnProfile(Authentication authentication){
        return userService.getOwnProfile(authentication);
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateProfile(
            @RequestBody UserRequestDTO userRequestDTO,
            Authentication authentication,
            HttpServletResponse response
    ){
        ResponseCookie jwtCookie = userService.updateProfile(userRequestDTO, authentication);

        response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok(new AuthResponseDTO("JWT stored in cookie", userRequestDTO.username()));
    }

    @PatchMapping("/password")
    public void updatePassword(
            @RequestBody PasswordUpdateDTO passwordUpdateDTO,
            Authentication authentication
    ){
        try{
            userService.updatePassword(passwordUpdateDTO, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/publisher/{id}")
    public UserDTO getPublisherProfile(@PathVariable Long id){
        return userService.getPublisherById(id);
    }
}
