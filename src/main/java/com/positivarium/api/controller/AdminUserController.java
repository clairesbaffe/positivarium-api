package com.positivarium.api.controller;

import com.positivarium.api.dto.UserWithRolesDTO;
import com.positivarium.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("This is ok, you can get in");
    }

    @GetMapping("/")
    public Page<UserWithRolesDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/{username}")
    public UserWithRolesDTO getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PatchMapping("/ban/{username}")
    public void banUser(@PathVariable String username){
        userService.ban(username);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PatchMapping("/unban/{username}")
    public void unbanUser(@PathVariable String username){
        userService.unban(username);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PutMapping("/publisher/{username}")
    public void grantPublisher(@PathVariable String username){
        userService.grantPublisher(username);
    }

    @PreAuthorize("!hasRole('BAN')")
    @PutMapping("/admin/{username}")
    public void grantAdmin(@PathVariable String username){
        userService.grantAdmin(username);
    }

}
