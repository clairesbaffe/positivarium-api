package com.positivarium.api.service;

import com.positivarium.api.config.JwtTokenProvider;
import com.positivarium.api.dto.*;
import com.positivarium.api.entity.Role;
import com.positivarium.api.entity.User;
import com.positivarium.api.exception.*;
import com.positivarium.api.mapping.UserMapping;
import com.positivarium.api.mapping.UserWithRolesMapping;
import com.positivarium.api.repository.RoleRepository;
import com.positivarium.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{10,}$";

    List<String> COMMON_PASSWORDS = new ArrayList<>(List.of(
            "password", "123456", "123456789", "12345678", "12345", "qwerty", "abc123",
            "football", "dragon", "111111", "iloveyou", "master", "sunshine", "passw0rd", "123123",
            "654321", "azerty", "azertyuiop", "motdepasse", "bonjour", "soleil", "chocolat", "123456",
            "12345678", "123456789", "qwertz", "admin", "admin123", "toto",
            "prenom", "nom", "paris", "marseille", "france", "monchien", "jesuis123", "jean123", "bonjour123",
            "jean", "pierre", "michel", "alain", "philippe",
            "marie", "nathalie", "isabelle", "christine", "sylvie"
    ));

    Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapping userMapping;
    private final UserWithRolesMapping userWithRolesMapping;
    private final JwtTokenProvider jwtTokenProvider;

    public User getCurrentUser(Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        if(username == null) throw new ResourceNotFoundException("User not found");

        return getUser(username);
    }

    public String validatePassword(String password) {
        if (password == null) return "Null";
        else if(password.length() < 10) return "Not long enough";

        for (String commonPassword : COMMON_PASSWORDS) {
            if (password.toLowerCase().contains(commonPassword.toLowerCase())) {
                return "Contains common password";
            }
        }

        Matcher matcher = pattern.matcher(password);
        if(matcher.matches()) return "OK";
        else return "Not complex enough";
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Page<UserWithRolesDTO> getAllUsers(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userWithRolesMapping::entityToDto);
    }

    public UserWithRolesDTO getUserById(Long id){
        User user = findUserById(id);
        return userWithRolesMapping.entityToDto(user);
    }

    public UserWithRolesDTO getUserByUsername(String username){
        User user = findUserByUsername(username);
        if(user == null) throw new ResourceNotFoundException("User not found");
        return userWithRolesMapping.entityToDto(user);
    }

    public UserWithRolesDTO getOwnProfile(Authentication authentication){
        User user = getCurrentUser(authentication);
        return getUserById(user.getId());
    }

    public UserDTO getPublisherByUsername(String publisherUsername, Authentication authentication){
        String username = authentication != null && authentication.isAuthenticated() ? authentication.getName() : null;
        User user = username == null ? null : getUser(username);

        User publisher = userRepository.findByUsernameAndRolesNameContaining(publisherUsername, "ROLE_PUBLISHER")
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Boolean isFollowed = user != null && userRepository.userFollowsPublisher(user.getId(), publisher.getId());
        return userMapping.entityToDtoWithIsFollowed(publisher, isFollowed);
    }

    public void registerNewUserAccount(UserRequestDTO user) {
        // username unicity check
        User matchedUsername = userRepository.findByUsername(user.username());
        if(matchedUsername != null) throw new UsernameAlreadyTakenException();

        User newUser = User.builder()
                .username(user.username())
                .build();

        // check password strength
        String passwordValidation = validatePassword(user.password());
        if(!passwordValidation.equals("OK"))
            throw new PasswordNotComplexEnoughException(passwordValidation);

        // set default role
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        List<Role> rolesList = new ArrayList<>(Collections.singleton(defaultRole));
        newUser.setRoles(rolesList);

        newUser.setPassword(bCryptPasswordEncoder.encode(user.password()));
        userRepository.save(newUser);
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<String> getUserRoles(String username) {
        return userRepository.findByUsername(username)
                .getRoles()
                .stream()
                .map(Role::getName).toList();
    }

    public List<String> getUserRoles(User user) {
        if (user == null) throw new ResourceNotFoundException("User not found");
        return user.getRoles().stream().map(Role::getName).toList();
    }


    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserRoles(String username, List<String> roleNames){
        User user = userRepository.findByUsername(username);
        if(user == null) throw new ResourceNotFoundException("User not found with username : " + username);

        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")))
                .toList();
        user.setRoles(roles);

        userRepository.save(user);
    }

    public void ban(String username){
        List<String> roles = getUserRoles(username);
        if(roles.contains("ROLE_BAN")) throw new InvalidUserStateException("User is already banned");
        if(roles.contains("ROLE_ADMIN")) throw new InvalidTargetUserException("Admins cannot be banned");

        roles.add("ROLE_BAN");
        updateUserRoles(username, roles);
    }

    public void unban(String username){
        List<String> roles = getUserRoles(username);
        if(!roles.contains("ROLE_BAN")) throw new InvalidUserStateException("User is not currently banned");

        roles.remove("ROLE_BAN");
        updateUserRoles(username, roles);
    }

    public void grantPublisher(String username){
        List<String> roles = getUserRoles(username);
        if(roles.contains("ROLE_BAN")) throw new UserIsBannedException("Banned users cannot be granted publisher");
        if(roles.contains("ROLE_PUBLISHER")) throw new InvalidUserStateException("User is already a publisher");
        if(!roles.contains("ROLE_USER")) throw new InvalidTargetUserException("Only users can become publisher");

        roles.clear();
        roles.add("ROLE_PUBLISHER");
        updateUserRoles(username, roles);
    }

    public void grantAdmin(String username){
        List<String> roles = getUserRoles(username);
        if(roles.contains("ROLE_BAN")) throw new UserIsBannedException("Banned users cannot be granted admin");
        if(roles.contains("ROLE_ADMIN")) throw new InvalidUserStateException("User is already an admin");

        roles.clear();
        roles.add("ROLE_ADMIN");
        updateUserRoles(username, roles);
    }

    public AuthResponseDTO updateProfile(UserRequestDTO userRequestDTO, Authentication authentication){
        User matchedUsername = userRepository.findByUsername(userRequestDTO.username());
        if(matchedUsername != null) throw new UsernameAlreadyTakenException();

        User user = getCurrentUser(authentication);
        user.setUsername(userRequestDTO.username());
        user.setDescription(userRequestDTO.description());
        userRepository.save(user);

        List<String> roles = getUserRoles(user);

        String token = jwtTokenProvider.generateToken(user.getUsername(), roles);

        return new AuthResponseDTO("JWT generated", token, user.getUsername());
    }

    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO, Authentication authentication){
        String passwordValidation = validatePassword(passwordUpdateDTO.newPassword());
        if(!passwordValidation.equals("OK"))
            throw new PasswordNotComplexEnoughException(passwordValidation);

        User user = getCurrentUser(authentication);
        if (bCryptPasswordEncoder.matches(passwordUpdateDTO.oldPassword(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(passwordUpdateDTO.newPassword()));
            userRepository.save(user);
        } else {
            throw new InvalidCredentialsException("Old password does not match");
        }
    }

    public void deleteAccount(Authentication authentication, String password){
        User user = getCurrentUser(authentication);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            userRepository.deleteById(user.getId());
        } else {
            throw new InvalidCredentialsException("Password does not match");
        }
    }

}

