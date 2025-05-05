package com.positivarium.api.controller;

import com.positivarium.api.dto.UserDTO;
import com.positivarium.api.dto.UserWithRolesDTO;
import com.positivarium.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/")
    public UserWithRolesDTO getOwnProfile(Authentication authentication){
        return userService.getOwnProfile(authentication);
    }

    @GetMapping("/publisher/{id}")
    public UserDTO getPublisherProfile(@PathVariable Long id){
        try{
            return userService.getPublisherById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
