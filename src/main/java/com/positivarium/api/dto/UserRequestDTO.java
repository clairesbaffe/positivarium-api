package com.positivarium.api.dto;

public record UserRequestDTO(
        String username,
        String email,
        String description,
        String password
){
}
