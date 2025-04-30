package com.positivarium.api.controller;

import com.positivarium.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/ban/{username}")
    public void banUser(@PathVariable String username){
        try{
            userService.ban(username);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/unban/{username}")
    public void unbanUser(@PathVariable String username){
        try{
            userService.unban(username);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/publisher/{username}")
    public void grantPublisher(@PathVariable String username){
        try{
            userService.grantPublisher(username);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/admin/{username}")
    public void grantAdmin(@PathVariable String username){
        try{
            userService.grantAdmin(username);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
