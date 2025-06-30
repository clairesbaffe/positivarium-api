package com.positivarium.api.controller;

import com.positivarium.api.dto.UserRequestDTO;
import com.positivarium.api.entity.User;
import com.positivarium.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO user) {
        userService.registerNewUserAccount(user);
        return ResponseEntity.ok("User registered successfully");
    }

}