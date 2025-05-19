package com.positivarium.api.mapping;

import com.positivarium.api.dto.UserDTO;
import com.positivarium.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapping {

    public UserDTO entityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .description(user.getDescription())
                .build();
    }
}
