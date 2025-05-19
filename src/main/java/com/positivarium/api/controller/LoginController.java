package com.positivarium.api.controller;

import com.positivarium.api.config.JwtTokenProvider;
import com.positivarium.api.dto.AuthResponseDTO;
import com.positivarium.api.dto.LoginDTO;
import com.positivarium.api.entity.User;
import com.positivarium.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDto, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Gather infos on user
            String userName = loginDto.getUsername();
            User user = userService.getUser(loginDto.getUsername());

            List<String> roles = userService.getUserRoles(user);

            // Create security token for user
            String token = jwtTokenProvider.generateToken(userName, roles);

            AuthResponseDTO responseDTO = new AuthResponseDTO("JWT generated", token, userName);
            return ResponseEntity.ok(responseDTO);
        } catch (AuthenticationException e) {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", "Invalid username or password");
            errorBody.put("code", "AUTH_FAILED");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }
    }

}
