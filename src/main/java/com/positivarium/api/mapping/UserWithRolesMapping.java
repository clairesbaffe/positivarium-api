package com.positivarium.api.mapping;

import com.positivarium.api.dto.UserWithRolesDTO;
import com.positivarium.api.entity.Role;
import com.positivarium.api.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserWithRolesMapping {

    public UserWithRolesDTO entityToDto(User user){
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserWithRolesDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }
}
