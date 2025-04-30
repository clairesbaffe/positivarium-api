package com.positivarium.api.service;

import com.positivarium.api.entity.Role;
import com.positivarium.api.entity.User;
import com.positivarium.api.repository.RoleRepository;
import com.positivarium.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User registerNewUserAccount(User user) {
        // set default role
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        List<Role> rolesList = new ArrayList<>(Collections.singleton(defaultRole));
        user.setRoles(rolesList);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public Boolean following(Long userId, Long publisherId){
        return userRepository.userFollowsPublisher(userId, publisherId);
    }

    public Page<User> findFollowing(Long userId, Pageable pageable){
        return userRepository.findUsersFollowedBy(userId, pageable);
    }

    public User getUserByToken(String token) {
        User user = userRepository.findByClaimToken(token);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new UsernameNotFoundException("Token expired");
        } else if (user.isEnabled()) {
            throw new UsernameNotFoundException("User already enabled");
        }

        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<String> getUserRoles(String username) {
        return userRepository.findByUsername(username)
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    public List<String> getUserRoles(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }


    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserRoles(String username, List<String> roleNames) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new Exception("User not found with username : " + username);
        }

        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toList());

        user.setRoles(roles);

        userRepository.save(user);
    }

    public void ban(String username) throws Exception {
        List<String> roles = getUserRoles(username);
        if(!roles.contains("ROLE_ADMIN")){
            if(!roles.contains("ROLE_BAN")){
                roles.add("ROLE_BAN");
                try{
                    updateUserRoles(username, roles);
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            } else throw new Exception("User is already banned");
        } else throw new Exception("Admins cannot be banned");
    }

    public void unban(String username) throws Exception {
        List<String> roles = getUserRoles(username);
        if(roles.contains("ROLE_BAN")){
            roles.remove("ROLE_BAN");
            try{
                updateUserRoles(username, roles);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        } else throw new Exception("User is not banned");
    }

    public void grantPublisher(String username) throws Exception {
        List<String> roles = getUserRoles(username);
        if(!roles.contains("ROLE_BAN")){
            if(roles.contains("ROLE_USER")){
                if(!roles.contains("ROLE_PUBLISHER")){
                    roles.clear();
                    roles.add("ROLE_PUBLISHER");
                    try{
                        updateUserRoles(username, roles);
                    } catch (Exception e){
                        throw new RuntimeException(e);
                    }
                } else throw new Exception("User is already publisher");
            } else throw new Exception("Only users can become publisher");
        } else throw new Exception("Banned users cannot be granted publisher");
    }

    public void grantAdmin(String username) throws Exception {
        List<String> roles = getUserRoles(username);
        if(!roles.contains("ROLE_BAN")){
            if(!roles.contains("ROLE_ADMIN")){
                roles.clear();
                roles.add("ROLE_ADMIN");
                try{
                    updateUserRoles(username, roles);
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            } else throw new Exception("User is already admin");
        } else throw new Exception("Banned users cannot be granted admin");
    }

}

