package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String username,
        String description,
        Boolean isFollowed
) {
}
