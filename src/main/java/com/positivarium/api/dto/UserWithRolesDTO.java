package com.positivarium.api.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserWithRolesDTO(
        Long id,
        String username,
        String description,
        Set<String> roles
){
}
