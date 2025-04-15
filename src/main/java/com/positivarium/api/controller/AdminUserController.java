package com.positivarium.api.controller;

import com.positivarium.api.entity.Role;
import com.positivarium.api.entity.User;
import com.positivarium.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("This is ok, you can get in");
    }

    @GetMapping("/ban")
    public void banUser(String username){
        List<String> roles = userService.getUserRoles(username);
        if(!roles.contains("ROLE_ADMIN")){
            roles.add("ROLE_BAN");
            try{
                User user = userService.updateUserRoles(username, roles);
            } catch (Exception e){

            }
        }
    }

    @GetMapping("/unban")
    public void unbanUser(String username){
        List<String> roles = userService.getUserRoles(username);
        if(!roles.contains("ROLE_ADMIN")){
            roles.remove("ROLE_BAN");
            try{
                User user = userService.updateUserRoles(username, roles);
            } catch (Exception e){

            }
        }
    }

    @GetMapping("/publisher")
    public void grantPublisher(String username){
        List<String> roles = userService.getUserRoles(username);
        if(roles.contains("ROLE_USER") && !roles.contains("ROLE_BAN")){
            roles.clear();
            roles.add("ROLE_PUBLISHER");
            try{
                User user = userService.updateUserRoles(username, roles);
            } catch (Exception e){

            }
        }
    }

    @GetMapping("/admin")
    public void grantAdmin(String username){
        List<String> roles = userService.getUserRoles(username);
        if(!roles.contains("ROLE_ADMIN") && !roles.contains("ROLE_BAN")){
            roles.clear();
            roles.add("ROLE_ADMIN");
            try{
                User user = userService.updateUserRoles(username, roles);
            } catch (Exception e){

            }
        }
    }

}
