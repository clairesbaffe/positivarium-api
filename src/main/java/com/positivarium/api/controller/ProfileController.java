package com.positivarium.api.controller;

import com.positivarium.api.dto.*;
import com.positivarium.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    public AuthResponseDTO updateProfile(
            @RequestBody UserRequestDTO userRequestDTO,
            Authentication authentication,
            HttpServletResponse response
    ){
        return userService.updateProfile(userRequestDTO, authentication);
    }

    @PatchMapping("/password")
    public void updatePassword(
            @RequestBody PasswordUpdateDTO passwordUpdateDTO,
            Authentication authentication
    ){
        userService.updatePassword(passwordUpdateDTO, authentication);
    }

    @GetMapping("/publisher/{id}")
    public UserDTO getPublisherProfile(@PathVariable Long id){
        return userService.getPublisherById(id);
    }
}
