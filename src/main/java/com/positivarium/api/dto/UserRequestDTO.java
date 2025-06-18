package com.positivarium.api.dto;

public record UserRequestDTO(
        String username,
        String description,
        String password
){
}
