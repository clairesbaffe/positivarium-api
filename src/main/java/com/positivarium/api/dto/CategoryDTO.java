package com.positivarium.api.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(
        Long id,
        String name,
        String generalCategory
) {
}