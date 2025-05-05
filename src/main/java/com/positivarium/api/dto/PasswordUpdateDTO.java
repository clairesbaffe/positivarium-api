package com.positivarium.api.dto;

public record PasswordUpdateDTO(
        String oldPassword,
        String newPassword
){
}
